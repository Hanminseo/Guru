package com.example.swu_2

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList


class Frag02 : Fragment() {
    // 변수
    lateinit var plusBtn: ImageButton
    lateinit var dbManager: DBManager
    lateinit var sqlDB: SQLiteDatabase
    lateinit var mAdapter: RecyclerAdapter
    lateinit var studytimeTv: TextView
    lateinit var dateTv: TextView
    lateinit var targetTime: TextView
    lateinit var targetTimeBtn: ImageButton
    lateinit var pgbar: ProgressBar
    lateinit var pgbarTv: TextView
    lateinit var totalTime: String
    var timepick: String = ""
    var tHour: Int = 0
    var tMin: Int = 0
    var tSec: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // frag02 xml로 화면 구성
        val view: View = inflater.inflate(R.layout.frag02, container, false)

        // 변수
        plusBtn = view.findViewById(R.id.plusBtn)
        studytimeTv = view.findViewById(R.id.studytimeTv)
        dateTv = view.findViewById(R.id.dateTv)
        targetTime = view.findViewById(R.id.targetTime)
        targetTimeBtn = view.findViewById(R.id.targetTimeBtn)
        pgbar = view.findViewById(R.id.pgbar)
        pgbarTv = view.findViewById(R.id.pgbarTv)

        var timeList = ArrayList<Time>()

        dbManager = DBManager(context?.applicationContext, "itemDB", null, 1)

        // 어댑터 생성
        mAdapter = RecyclerAdapter(requireContext(), timeList)
        var recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.adapter = mAdapter

        // db에서 리사이클뷰에 넣을 데이터 로드
        sqlDB = dbManager.readableDatabase
        var cursor: Cursor
        cursor = sqlDB.rawQuery("SELECT * FROM stopwatchTBL;", null)
        while (cursor.moveToNext()) {
            var getSubjectString = cursor.getString(0)
            var getTimeString = cursor.getString(1)
            // 목표 시간을 정한 경우에만 목표시간을 text로 전달하기
            if(cursor.getString(2) != "0"){
                targetTime.text = cursor.getString(2)
            }
            // 공부 시간이 0으로 저장된 항목을 제외하고 리스트에 추가
            else if (getTimeString != "00 : 00 : 00") {
                mAdapter.addItem(Time(getSubjectString, getTimeString))
                tHour += Integer.parseInt(getTimeString.substring(0, 2))
                tMin += Integer.parseInt(getTimeString.substring(5, 7))
                tSec += Integer.parseInt(getTimeString.substring(10, 12))
            }
            onSettingProgress()
        }
        cursor.close()
        sqlDB.close()
        mAdapter.notifyDataSetChanged()

        // 총 공부량 합산 보이기
        tMin += (tSec / 60)
        tSec = tSec % 60
        tHour += (tMin / 60)
        tMin = tMin % 60
        totalTime = String.format("%02d : %02d : %02d", tHour, tMin, tSec)
        studytimeTv.text = totalTime


        // plusBtn 누르면 스톱워치 액티비티로 전환
        plusBtn.setOnClickListener {
            // 목표 시간을 설정해야만 스톱워치 기능을 이용할 수 있음
            if (targetTime.text == " 00시간 00분 ") {
                Toast.makeText(context?.applicationContext, "목표 시간을 설정하세요", Toast.LENGTH_SHORT)
                    .show()
            } else {
                (activity as MainActivity).replaceFragment(StopWatch2())
            }

        }

        // targetTimeBtn 버튼 클릭 시 목표 시간 셋팅할 수 있는 스피너 생성
        targetTimeBtn.setOnClickListener {

                var listener = TimePickerDialog.OnTimeSetListener { timePicker, i, i2 ->
                    timepick = String.format("%02d시간 %02d분", i, i2)
                    targetTime.text = timepick
                    sqlDB = dbManager.writableDatabase
                    // 모든 리스트 뷰의 체크 박스를 UNCHECKED로 초기화함
                    sqlDB.execSQL("INSERT INTO stopwatchTBL VALUES ('', '00 : 00 : 00', '"+timepick+"');")
                    sqlDB.close()
                    onSettingProgress()
                }

                val dialog = TimePickerDialog(
                    activity,
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                    listener,
                    0,
                    0,
                    true
                )

                dialog.setTitle(" 목표 시간 설정하기 ")
                dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                dialog.show()
            }

        return view
    }

    //frag에서 뒤로가기 버튼 누르면 메인(Frag01)으로 이동
    private lateinit var callback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (activity as MainActivity).replaceFragment(Frag01())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }


    // 목표 시간 대비 공부 시간 프로그레스 바 설정
    private fun onSettingProgress() {

        if (targetTime.text != "") {
            var total = (tHour * 60 + tMin).toDouble()
            var target = (targetTime.text.substring(0, 2).toDouble()) * 60 + targetTime.text.substring(5, 7).toDouble()

            if (target != 0.0) {
                var prog = (total / target) * 100
                var percent = prog.toInt()
                pgbar.setProgress(percent)
                if (percent >= 100) {
                    percent = 100
                }
                pgbarTv.text = (percent).toString() + "%"
            }
        } else {
            pgbar.setProgress(0)
        }
    }

}
