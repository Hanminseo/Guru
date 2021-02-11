package com.example.swu_2

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

class Frag04 : Fragment() {

    //변수
    lateinit var todolist_SV: ListView
    lateinit var what_todo: EditText
    lateinit var savetodo: ImageButton
    lateinit var deletetodo: ImageButton
    lateinit var edtBtn: ImageButton
    lateinit var titleTv: TextView

    lateinit var adapter: ArrayAdapter<String>
    lateinit var listItem: ArrayList<String>
    lateinit var getContent: String
    lateinit var getDate: String
    lateinit var content: String

    // db 변수
    lateinit var dbManager: DBManager
    lateinit var sqlDB: SQLiteDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // activity_calendarlist로 화면 구성
        val view: View = inflater.inflate(R.layout.activity_calendarlist, container, false)

        todolist_SV = view.findViewById(R.id.todolist_SV)
        what_todo = view.findViewById(R.id.what_todo)
        savetodo = view.findViewById(R.id.savetodo)
        deletetodo = view.findViewById(R.id.deletetodo)
        listItem = ArrayList<String>()
        edtBtn = view.findViewById(R.id.editBtn)
        titleTv = view.findViewById(R.id.titleTv)

        //어댑터 연결
        adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_multiple_choice,
            listItem
        )
        todolist_SV.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE)
        todolist_SV.setAdapter(adapter)

        //db에서 데이터 로드
        dbManager = DBManager(context?.applicationContext, "itemDB", null, 1)
        sqlDB = dbManager.readableDatabase

        var cursor: Cursor
        // calTBL에서 sign이 1인 행의 날짜를 가져옴
        cursor = sqlDB.rawQuery("SELECT date FROM calTBL WHERE sign='1';", null)
        while (cursor.moveToNext()) {
            getDate = cursor.getString(0)
            titleTv.text = "${getDate} 일정"
        }
        // calTBL에서 sign이 1인 행을 삭제함
        sqlDB.execSQL("DELETE FROM calTBL WHERE sign='1';")

        // 가져온 날짜를 가진 행의 content를 가져옴
        cursor = sqlDB.rawQuery("SELECT content FROM calTBL WHERE date='${getDate}';", null)
        while (cursor.moveToNext()) {
            getContent = cursor.getString(0)
            if (getContent != "null") {
                // 내용이 있다면 리스트에 추가
                listItem.add(getContent)
            }
        }
        cursor.close()
        sqlDB.close()

        adapter.notifyDataSetChanged()

        //저장 버튼
        savetodo.setOnClickListener {
            var edtlist: String = what_todo.getText().toString()

            // db에 항목 저장
            sqlDB = dbManager.writableDatabase
            sqlDB.execSQL("INSERT INTO calTBL VALUES ('${getDate}', '${edtlist}', '0');")
            sqlDB.close()

            // 리스트에 항목 추가
            listItem.add(edtlist)
            adapter.notifyDataSetChanged()
            what_todo.setText("")
        }

        // 체크된 항목 삭제
        deletetodo.setOnClickListener {
            val checkedItems = todolist_SV.getCheckedItemPositions()
            val count = adapter.getCount()
            for (i in count - 1 downTo 0) {
                if (checkedItems.get(i)) {
                    // db에서 항목 삭제
                    sqlDB = dbManager.writableDatabase
                    sqlDB.execSQL("DELETE FROM calTBL WHERE content='" + listItem.get(i) + "';")
                    sqlDB.close()

                    // 리스트에서 항목 제거
                    listItem.removeAt(i)
                }
            }
            todolist_SV.clearChoices()
            adapter.notifyDataSetChanged()
        }

        // 에디트 버튼을 누르면 Frag01로 이동동
        edtBtn.setOnClickListener {
            (activity as MainActivity).replaceFragment(Frag01())
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
}