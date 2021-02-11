package com.example.swu_2

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Gonggi : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    var emailAuth : FirebaseAuth? = null
    var emailFirestore : FirebaseFirestore? = null

    lateinit var notice_w : EditText //입력
    lateinit var notice_r: TextView //출력
    lateinit var btnNotice1 : Button //버튼
    lateinit var btnNotice2 : Button
    lateinit var gonggi_home: ImageButton

    lateinit var dbManager: DBManager
    lateinit var sqlDB: SQLiteDatabase
    lateinit var noticeBtn: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gongji)

        //db접근
        auth = FirebaseAuth.getInstance()

        //firestore,auth 접근
        emailAuth = FirebaseAuth.getInstance()
        emailFirestore = FirebaseFirestore.getInstance()

        dbManager = DBManager(this, "itemDB", null, 1)
        noticeBtn = findViewById(R.id.noticeBtn)


        //사용자가 입력한 값
        val notice_w = findViewById<EditText>(R.id.notice_w)
        val notice_r = findViewById<TextView>(R.id.notice_r)
        val btnNotice1 = findViewById<Button>(R.id.btnNotice1)
        val btnNotice2 = findViewById<Button>(R.id.btnNotice2)
        val gonggi_home = findViewById<ImageButton>(R.id.g_home)

        btnNotice2.setOnClickListener {

            //계정 uid가져오기
            val store_uid = emailAuth?.uid

            //member컬렉션에 일치하는 uid의 문서 객체로 가져오기
            val docRef = emailFirestore?.collection("member")?.document(store_uid.toString())
            docRef?.get()?.addOnSuccessListener {
                val member = it.toObject(Member::class.java)

                //객체 내용 중 이름이랑 그룹이름 저장

                val name = member?.storeName
                val groupcode = member?.storeGroup

                //객체를 통해 공지내용 db에 저장

                var groupInfo = group_m()

                groupInfo.storeContent = notice_w.getText().toString()
                groupInfo.storeName = name

                emailFirestore?.collection("group")?.document(groupcode.toString())?.set(groupInfo)
            }
        }
        btnNotice1.setOnClickListener {

            //계정 uid가져오기
            val store_uid = emailAuth?.uid

            //member컬렉션에 일치하는 uid의 문서 객체로 가져오기
            val docRef = emailFirestore?.collection("member")?.document(store_uid.toString())
            docRef?.get()?.addOnSuccessListener {
                val member = it.toObject(Member::class.java)

                //그룹코드 가져오기

                val group_code = member?.storeGroup

              //  var groupInfo = group_m()


                //group컬렉션에 일치하는 그룹코드의 문서 객체로 가져오기
                val docRef1 = emailFirestore?.collection("group")?.document(group_code.toString())
                docRef1?.get()?.addOnSuccessListener {
                    val group_m = it.toObject(group_m::class.java)

                    //공지 작성자와 내용 같이 출력
                    notice_r.setText(group_m?.storeName+ " : " + group_m?.storeContent)

                    // noticeTBL에 공지내용 추가
                    sqlDB = dbManager.writableDatabase
                    if(group_m?.storeContent.toString() != "") {
                        sqlDB.execSQL("INSERT INTO noticeTBL VALUES ('"+notice_r.text.toString()+"');")
                    }
                    sqlDB.close()
                }
            }
        }

        // 버튼 클릭시 NoticeBoard로 이동
        noticeBtn.setOnClickListener {
            var intent = Intent(this, NoticeBoard::class.java)
            startActivity(intent)
        }

        //버튼 클릭시 메인홈으로 이동
        gonggi_home.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }
    }

    //메인으로 돌아가기
    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

