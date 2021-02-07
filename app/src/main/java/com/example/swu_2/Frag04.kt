package com.example.swu_2

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import androidx.fragment.app.Fragment

class Frag04 : Fragment() {

    lateinit var todolist_SV : ListView
    lateinit var what_todo : EditText
    lateinit var savetodo : ImageButton
    lateinit var deletetodo : ImageButton

    lateinit var adapter: ArrayAdapter<String>
    lateinit var listItem: ArrayList<String>
    lateinit var getItemString: String

    //db
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

        adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_multiple_choice,
            listItem
        )
        todolist_SV.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE)
        todolist_SV.setAdapter(adapter)

        dbManager = DBManager(context?.applicationContext, "calendardb", null, 1)
        sqlDB = dbManager.readableDatabase

        var cursor: Cursor
        cursor = sqlDB.rawQuery("SELECT item FROM calendarTBL;", null)
        while (cursor.moveToNext()) {
            getItemString = cursor.getString(0)
            listItem.add(getItemString)
        }
        cursor.close()
        sqlDB.close()
        adapter.notifyDataSetChanged()

        savetodo.setOnClickListener {
            var edtlist: String = what_todo.getText().toString()

            // db에 항목 저장
            sqlDB = dbManager.writableDatabase
            sqlDB.execSQL("INSERT INTO calendarTBL VALUES ('" + edtlist + "', 'UNCHECKED');")
            sqlDB.close()

            // 리스트에 항목 추가
            listItem.add(edtlist)
            adapter.notifyDataSetChanged()
            what_todo.setText("")
        }

        deletetodo.setOnClickListener {
            val checkedItems = todolist_SV.getCheckedItemPositions()
            val count = adapter.getCount()
            for (i in count - 1 downTo 0) {
                if (checkedItems.get(i)) {
                    // db에서 항목 삭제
                    sqlDB = dbManager.writableDatabase
                    sqlDB.execSQL("DELETE FROM calendarTBL WHERE item='" + listItem.get(i) + "';")
                    sqlDB.close()

                    // 리스트에서 항목 제거
                    listItem.removeAt(i)
                }
            }
            todolist_SV.clearChoices()
            adapter.notifyDataSetChanged()
        }

        return view
    }
}