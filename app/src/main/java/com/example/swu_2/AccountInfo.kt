package com.example.swu_2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AccountInfo : AppCompatActivity() {

    lateinit var pswdbtn : Button

    lateinit var userName: TextView
    lateinit var userEmail: TextView
    lateinit var groupNum: TextView
    lateinit var birth: TextView

    //firebase,db 연결
    var fireEmail : FirebaseAuth? = null
    var firestore : FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_info)

        userName = findViewById(R.id.userName)
        userEmail = findViewById(R.id.userEmail)
        groupNum = findViewById(R.id.GroupNum)
        birth = findViewById(R.id.birth)

        //계정 초기화
        fireEmail = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        //계정 uid 가져오기
        val store_uid = fireEmail?.uid

        //member컬렉션에 일치하는 uid의 문서 객체로 가져오기
        val docRef = firestore?.collection("member")?.document(store_uid.toString())
        docRef?.get()?.addOnSuccessListener {
            val member = it.toObject(Member::class.java)

            //객체 내용 띄우기
            userName.setText(member?.storeName)
            userEmail.setText(member?.storeEmail)
            groupNum.setText(member?.storeGroup)
            birth.setText(member?.storeBirth)
        }

        pswdbtn = findViewById(R.id.pswdbtn)
        pswdbtn.setOnClickListener {
            startActivity(Intent(this,Password::class.java))
        }
    }
}