package com.example.swu_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class TodoList : AppCompatActivity() {

    lateinit var listPlus : ImageButton
    lateinit var listDelete : ImageButton
    lateinit var edtList : EditText
    lateinit var todoListView : ListView
    lateinit var adapter: ArrayAdapter<String>
    lateinit var listItem:ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)

        listPlus = findViewById(R.id.listPlus)
        listDelete = findViewById(R.id.listDelete)
        edtList = findViewById(R.id.edtList)
        listItem = ArrayList<String>()
        todoListView = findViewById(R.id.todoListView)

        listPlus.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View) {
                listItem.add(edtList.getText().toString())
                adapter.notifyDataSetChanged() // 변경되었음을 어답터에 알려준다.
                edtList.setText("")
            }
        })
        adapter = ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, listItem)
        todoListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE)
        todoListView.setAdapter(adapter)
        todoListView.setOnItemClickListener(object: AdapterView.OnItemClickListener {
            // 콜백매개변수는 순서대로 어댑터뷰, 해당 아이템의 뷰, 클릭한 순번, 항목의 아이디
            override fun onItemClick(adapterView: AdapterView<*>, view:View, i:Int, l:Long) {
                Toast.makeText(getApplicationContext(), listItem.get(i).toString() + " 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                listItem.removeAt(i)
                adapter.notifyDataSetChanged()
            }
        })
        listDelete.setOnClickListener {

        }
    }
}