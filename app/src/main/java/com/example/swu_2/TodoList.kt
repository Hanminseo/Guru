package com.example.swu_2


import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*


class TodoList : AppCompatActivity() {

    lateinit var listPlus: ImageButton
    lateinit var listDelete: ImageButton
    lateinit var edtList: EditText
    lateinit var todoListView: ListView
    lateinit var adapter: ArrayAdapter<String>
    lateinit var listItem: ArrayList<String>
    lateinit var editBtn: ImageButton

    lateinit var getItemString: String

    lateinit var dbManager: DBManager
    lateinit var sqlDB: SQLiteDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)

        listPlus = findViewById(R.id.listPlus)
        listDelete = findViewById(R.id.listDelete)
        edtList = findViewById(R.id.edtList)
        todoListView = findViewById(R.id.todoListView)
        editBtn = findViewById(R.id.editBtn)
        listItem = ArrayList<String>()


        // 어댑터 연결
        adapter = ArrayAdapter<String>(
            getApplicationContext(),
            android.R.layout.simple_list_item_multiple_choice,
            listItem
        )
        todoListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE)
        todoListView.setAdapter(adapter)

        // db에서 데이터 로드
        dbManager = DBManager(this, "itemDB", null, 1)
        sqlDB = dbManager.readableDatabase

        var cursor: Cursor
        cursor = sqlDB.rawQuery("SELECT item FROM itemTBL;", null)
        while (cursor.moveToNext()){
            getItemString = cursor.getString(0)
            listItem.add(getItemString)
        }
        cursor.close()
        sqlDB.close()
        adapter.notifyDataSetChanged()


        //리스트 추가
        listPlus.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                var edtlist: String = edtList.getText().toString()

                // db에 항목 저장
                sqlDB = dbManager.writableDatabase
                sqlDB.execSQL("INSERT INTO itemTBL VALUES ('" + edtlist + "', 'UNCHECKED');")
                sqlDB.close()

                // 리스트에 항목 추가
                listItem.add(edtlist)
                adapter.notifyDataSetChanged()
                edtList.setText("")
            }
        })

        //리스트 삭제
        listDelete.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val checkedItems = todoListView.getCheckedItemPositions()
                val count = adapter.getCount()
                for (i in count - 1 downTo 0) {
                    if (checkedItems.get(i)) {
                        // db에서 항목 삭제
                        sqlDB = dbManager.writableDatabase
                        sqlDB.execSQL("DELETE FROM itemTBL WHERE item='" + listItem.get(i) + "';")
                        sqlDB.close()

                        // 리스트에서 항목 제거
                        listItem.removeAt(i)
                    }
                }
                todoListView.clearChoices()
                adapter.notifyDataSetChanged()
            }
        })

        // 편집 버튼 클릭 시 메인 액티비티로 화면 전환
        editBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                // 인텐트 전달
                val OutIntent = Intent(applicationContext, MainActivity::class.java)
                startActivity(OutIntent)
            }
        })
    }


}