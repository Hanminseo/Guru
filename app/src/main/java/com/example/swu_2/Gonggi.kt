package com.example.swu_2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.microedition.khronos.egl.EGLDisplay

class Gonggi : AppCompatActivity() {

    lateinit var auth: FirebaseAuth


    var emailAuth : FirebaseAuth? = null
    var emailFirestore : FirebaseFirestore? = null

    lateinit var notice_w : EditText //입력
    lateinit var notice_r: TextView //출력
    lateinit var btnNotice1 : Button //버튼
    lateinit var btnNotice2 : Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gongji)


        auth = FirebaseAuth.getInstance()

        //firestore
        emailAuth = FirebaseAuth.getInstance()
        emailFirestore = FirebaseFirestore.getInstance()


        //사용자가 입력한 값
        val notice_w = findViewById<EditText>(R.id.notice_w)
        val notice_r = findViewById<TextView>(R.id.notice_r)
        val btnNotice1 = findViewById<Button>(R.id.btnNotice1)
        val btnNotice2 = findViewById<Button>(R.id.btnNotice2)


        btnNotice2.setOnClickListener {

            //계정 uid가져오기
            val store_uid = emailAuth?.uid

            //member컬렉션에 일치하는 uid의 문서 객체로 가져오기
            val docRef = emailFirestore?.collection("member")?.document(store_uid.toString())
            docRef?.get()?.addOnSuccessListener {
                val member = it.toObject(Member::class.java)

                //객체 내용 띄우기

                val name = member?.storeName
                val phone = member?.storeGroup

                //firestore 저장

                var groupInfo = group_m()

                groupInfo.storeContent = notice_w.getText().toString()



                emailFirestore?.collection("group")?.document(phone.toString())?.set(groupInfo)


            }

        }

        btnNotice1.setOnClickListener {

            //계정 uid가져오기
            val store_uid = emailAuth?.uid

            //member컬렉션에 일치하는 uid의 문서 객체로 가져오기
            val docRef = emailFirestore?.collection("member")?.document(store_uid.toString())
            docRef?.get()?.addOnSuccessListener {
                val member = it.toObject(Member::class.java)

                //객체 내용 띄우기

                val phone = member?.storeGroup

                var groupInfo = group_m()


                //member컬렉션에 일치하는 uid의 문서 객체로 가져오기
                val docRef1 = emailFirestore?.collection("group")?.document(phone.toString())
                docRef1?.get()?.addOnSuccessListener {
                    val group_m = it.toObject(group_m::class.java)
                    notice_r.setText(group_m?.storeContent)
                    // 리스트에 추가
                }






            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

