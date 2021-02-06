package com.example.swu_2



import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment


class TodoList2 : Fragment()  {

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

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // todoList xml로 화면 구성
        val view: View = inflater.inflate(R.layout.activity_todo_list, container, false)

        listPlus = view.findViewById(R.id.listPlus)
        listDelete = view.findViewById(R.id.listDelete)
        edtList = view.findViewById(R.id.edtList)
        todoListView = view.findViewById(R.id.todoListView)
        editBtn = view.findViewById(R.id.editBtn)
        listItem = ArrayList<String>()


        // 어댑터 연결
        adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_multiple_choice,
            listItem
        )
        todoListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE)
        todoListView.setAdapter(adapter)

        // db에서 데이터 로드
        dbManager = DBManager(context?.applicationContext, "itemDB", null, 1)
        sqlDB = dbManager.readableDatabase

        var cursor: Cursor
        cursor = sqlDB.rawQuery("SELECT item FROM todolistTBL;", null)
        while (cursor.moveToNext()) {
            getItemString = cursor.getString(0)
            listItem.add(getItemString)
        }
        cursor.close()
        sqlDB.close()
        adapter.notifyDataSetChanged()


        //리스트 추가
        listPlus.setOnClickListener {
            var edtlist: String = edtList.getText().toString()

            // db에 항목 저장
            sqlDB = dbManager.writableDatabase
            sqlDB.execSQL("INSERT INTO todolistTBL VALUES ('" + edtlist + "', 'UNCHECKED');")
            sqlDB.close()

            // 리스트에 항목 추가
            listItem.add(edtlist)
            adapter.notifyDataSetChanged()
            edtList.setText("")
        }


        //리스트 삭제
        listDelete.setOnClickListener {
            val checkedItems = todoListView.getCheckedItemPositions()
            val count = adapter.getCount()
            for (i in count - 1 downTo 0) {
                if (checkedItems.get(i)) {
                    // db에서 항목 삭제
                    sqlDB = dbManager.writableDatabase
                    sqlDB.execSQL("DELETE FROM todolistTBL WHERE item='" + listItem.get(i) + "';")
                    sqlDB.close()

                    // 리스트에서 항목 제거
                    listItem.removeAt(i)
                }
            }
            todoListView.clearChoices()
            adapter.notifyDataSetChanged()
        }


        // 편집 버튼 클릭 시 메인 액티비티로 화면 전환
        editBtn.setOnClickListener {
            // 인텐트 전달
            (activity as MainActivity).replaceFragment(Frag01())
        }


        return view
    }

}