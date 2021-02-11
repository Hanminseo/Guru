package com.example.swu_2

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class NoticeBoard : AppCompatActivity() {

    //변수
    lateinit var boardlist: ListView
    lateinit var adapter: ArrayAdapter<String>
    lateinit var listItem: ArrayList<String>
    lateinit var dbManager: DBManager
    lateinit var sqlDB: SQLiteDatabase
    lateinit var deleteBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notice_board)

        boardlist = findViewById(R.id.boardlist)
        listItem = ArrayList<String>()
        deleteBtn = findViewById(R.id.deleteBtn)
        dbManager = DBManager(this, "itemDB", null, 1)


        // 어댑터 연결
        adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            listItem
        )
        boardlist.setAdapter(adapter)

        sqlDB = dbManager.readableDatabase

        // db에서 데이터 로드
        var cursor: Cursor
        cursor = sqlDB.rawQuery("SELECT notice FROM noticeTBL;", null)
        while (cursor.moveToNext()) {
            var content = cursor.getString(0)
            listItem.add(content)
        }
        cursor.close()
        sqlDB.close()
        adapter.notifyDataSetChanged()

        // 삭제 버튼 클릭시 다 삭제
        deleteBtn.setOnClickListener {
            sqlDB = dbManager.writableDatabase
            sqlDB.execSQL("DELETE FROM noticeTBL;")
            sqlDB.close()

            // 리스트에서 전체 항목 제거
            listItem.clear()
            adapter.notifyDataSetChanged()

            Toast.makeText(this, "공지가 모두 삭제되었습니다", Toast.LENGTH_SHORT).show()
        }
    }
}