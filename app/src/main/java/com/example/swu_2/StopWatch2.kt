package com.example.swu_2

import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment

import java.util.*
import kotlin.concurrent.timer

class StopWatch2 : Fragment() {
    //변수
    var time = 0
    var timerTask: Timer? = null
    var isRunning = false

    lateinit var dbManager: DBManager
    lateinit var sqlDB: SQLiteDatabase

    lateinit var playbtn: ImageButton
    lateinit var stopwatch_time: TextView
    lateinit var subjectName: EditText
    lateinit var datestring: TextView
    lateinit var you_study: TextView
    lateinit var study_subject: TextView
    lateinit var you_studytime: TextView
    lateinit var i_studytime: TextView
    lateinit var uploadBtn: Button

    lateinit var inflater: LayoutInflater
    var hour = 0
    var min = 0
    var sec = 0
    var watch = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // activity_stop_watch2 xml로 화면 구성
        val view: View = inflater.inflate(R.layout.activity_stop_watch2, container, false)

        playbtn = view.findViewById(R.id.playbtn)
        stopwatch_time = view.findViewById(R.id.stopwatch_time)
        subjectName = view.findViewById(R.id.subjectName)
        datestring = view.findViewById(R.id.datestring)
        you_study = view.findViewById(R.id.you_study)
        study_subject = view.findViewById(R.id.study_subject)
        you_studytime = view.findViewById(R.id.you_studytime)
        i_studytime = view.findViewById(R.id.i_studytime)
        uploadBtn = view.findViewById(R.id.upload_btn)

        dbManager = DBManager(context?.applicationContext, "itemDB", null, 1)

        // 플레이버튼 클릭 리스너
        playbtn.setOnClickListener {
            isRunning = !isRunning

            if (isRunning) {
                start()
            } else {
                pause()
            }
        }
        // 현재 날짜 로드
        var now = LocalDate.now()

        var Strnow = now.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        datestring.text = Strnow

        // 등록 버튼 클릭 메소드
        uploadBtn.setOnClickListener {
            // 과목 명과 시간이 등록되어있지 않다면 토스트 메시지가 출력됨
            if (subjectName.text.toString() == "" ) {
                Toast.makeText(context?.applicationContext, "과목 명을 등록하고 시간을 측정해주십시오", Toast.LENGTH_SHORT).show()
            } else if(i_studytime.text.toString() == "00 : 00 : 00") {
                Toast.makeText(context?.applicationContext, "시간을 측정해주십시오", Toast.LENGTH_SHORT).show()
            } else {
                // db에 스톱워치 데이터(과목명, 공부 시간) 저장
                sqlDB = dbManager.writableDatabase
                sqlDB.execSQL(
                    "INSERT INTO stopwatchTBL VALUES ('" + subjectName.text.toString()
                            + "', '"+i_studytime.text.toString()+"', '0');"
                )
                sqlDB.close()

                // 등록 Toast 문구 출력 후 Frag02 액티비티로 돌아가기
                Toast.makeText(context?.applicationContext, "등록되었습니다", Toast.LENGTH_SHORT).show()
                (activity as MainActivity).replaceFragment(Frag02())
            }
        }
        return view
    }

    //시작 버튼 눌렀을때
    private fun start() {
        if (subjectName.text.toString() == "") {
            Toast.makeText(context?.applicationContext, "과목명을 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        playbtn.setImageResource(R.drawable.ic_noun_pause_2494384)
        study_subject.text = subjectName.text.toString()
        timerTask = timer(period = 10) {
            time++

            hour = (time / 360000) % 24
            min = time / 6000 % 60
            sec = time / 100 % 60
            watch = String.format("%02d : %02d : %02d", hour, min, sec)

            activity?.runOnUiThread {
                stopwatch_time.text = "$watch"
            }
        }
    }

    //멈춤 버튼 눌렀을때
    private fun pause() {
        playbtn.setImageResource(R.drawable.ic_noun_play_943866)
        timerTask?.cancel()

        i_studytime.text = "${watch}"
    }
}