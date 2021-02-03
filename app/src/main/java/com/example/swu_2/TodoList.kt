package com.example.swu_2


import android.content.Intent
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)

        listPlus = findViewById(R.id.listPlus)
        listDelete = findViewById(R.id.listDelete)
        edtList = findViewById(R.id.edtList)
        todoListView = findViewById(R.id.todoListView)
        editBtn = findViewById(R.id.editBtn)
        listItem = ArrayList<String>()


        //리스트 추가
        listPlus.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val edtlist = edtList.getText().toString()

                listItem.add(edtlist)
                adapter.notifyDataSetChanged()
                edtList.setText("")
            }
        })
        // Main에서 intent 값 가져오기
        val Inintent = getIntent()
        val item = Inintent.getSerializableExtra("ArrayList2") as? java.util.ArrayList<String>

        // main에서 가져온 리스트에 항목이 있으면 item으로 adapter 셋팅
        if (item != null) {
            listItem = item
        }

        adapter = ArrayAdapter<String>(
            getApplicationContext(),
            android.R.layout.simple_list_item_multiple_choice,
            listItem
        )
        todoListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE)
        todoListView.setAdapter(adapter)

        //리스트 삭제
        listDelete.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val checkedItems = todoListView.getCheckedItemPositions()
                val count = adapter.getCount()
                for (i in count - 1 downTo 0) {
                    if (checkedItems.get(i)) {
                        listItem.removeAt(i)
                    }
                }
                todoListView.clearChoices()
                adapter.notifyDataSetChanged()
            }
        })

        // 편집 버튼 클릭 시 메인 액티비티에 list를 전달하고 화면 전환
        editBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                // 인텐트 전달
                val OutIntent = Intent(applicationContext, MainActivity::class.java)
                OutIntent.putExtra("ArrayList", listItem)
                startActivityForResult(OutIntent, 0)
            }
        })

    }


}