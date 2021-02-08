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

class cal_test : AppCompatActivity() {

    lateinit var auth: FirebaseAuth


    var emailAuth : FirebaseAuth? = null
    var emailFirestore : FirebaseFirestore? = null

    lateinit var t1 : EditText //입력
    lateinit var t2: TextView //출력
    lateinit var b1 : Button //버튼
    lateinit var b2 : Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cal_test)

        auth = FirebaseAuth.getInstance()

        //firestore
        emailAuth = FirebaseAuth.getInstance()
        emailFirestore = FirebaseFirestore.getInstance()


        //사용자가 입력한 값
        val t1 = findViewById<EditText>(R.id.test_context)
        val t2 = findViewById<TextView>(R.id.test_context1)
        val b1 = findViewById<Button>(R.id.test_button)
        val b2 = findViewById<Button>(R.id.test_button1)

        //계정만들기
        b1.setOnClickListener {

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

                groupInfo.storeContent = t1.getText().toString()



                emailFirestore?.collection("group")?.document(phone.toString())?.set(groupInfo)





            }

        }

        b2.setOnClickListener {

            //계정 uid가져오기
            val store_uid = emailAuth?.uid

            //member컬렉션에 일치하는 uid의 문서 객체로 가져오기
            val docRef = emailFirestore?.collection("member")?.document(store_uid.toString())
            docRef?.get()?.addOnSuccessListener {
                val member = it.toObject(Member::class.java)

                //객체 내용 띄우기

                val phone = member?.storeGroup

          //      var groupInfo = group_m()


                //그룹코드 통해서 공지 가져오기
                val docRef1 = emailFirestore?.collection("group")?.document(phone.toString())
                docRef1?.get()?.addOnSuccessListener {
                    val group_m = it.toObject(group_m::class.java)
                    t2.setText(group_m?.storeContent)
                }






            }
        }
    }
}

