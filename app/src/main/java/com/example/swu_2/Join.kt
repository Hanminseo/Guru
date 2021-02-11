package com.example.swu_2

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Join : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    private val TAG : String = "Join"
    var emailAuth : FirebaseAuth? = null
    var emailFirestore : FirebaseFirestore? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.join)

        auth = FirebaseAuth.getInstance()

        //firestore,firebase접근용
        emailAuth = FirebaseAuth.getInstance()
        emailFirestore = FirebaseFirestore.getInstance()


        //id와 연결
        val email = findViewById<EditText>(R.id.Email)
        val password = findViewById<EditText>(R.id.Password)
        val btnOk = findViewById<Button>(R.id.btnOk)
        val name = findViewById<EditText>(R.id.Name)
        val birthday = findViewById<EditText>(R.id.Birthday)
        val groupnum = findViewById<EditText>(R.id.GroupNum)

        //계정만들기
        btnOk.setOnClickListener {

            //email,password 공백시 toast 띄우기
            if (email.text.toString().length == 0 || password.text.toString().length == 0) {
                Toast.makeText(this, "email 혹은 password를 입력하세요.", Toast.LENGTH_SHORT).show()
            }
            //그룹코드 6자리 이하 입력시 toast 띄우기
            else if (groupnum.text.toString().length <= 6) {
                Toast.makeText(this, "코드를 7자리 이상 입력하세요.", Toast.LENGTH_SHORT).show()
            }
            //그외 firebase에 저장
            else {
                auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "createUserWithEmail:success")
                            finish()
                            Toast.makeText(
                                this, "회원가입 완료", Toast.LENGTH_SHORT
                            ).show()



                            //firestore에 회원정보 저장, 그룹코드 전용 문서 생성
                            if (true) {
                                var userInfo = Member() //회원정보 저장 객체
                              //  var groupInfo = group_m() //공지내용 저장 객체

                                //회원정보 객체에 저장
                                userInfo.storeUid = emailAuth?.uid
                                userInfo.storeEmail = emailAuth?.currentUser?.email
                                userInfo.storeBirth = birthday.getText().toString()
                                userInfo.storeName = name.getText().toString()
                                userInfo.storeGroup = groupnum.getText().toString()

                                //객체내용 firestore에 저장
                                emailFirestore?.collection("member")?.document(emailAuth?.uid.toString())?.set(userInfo)


                            } else {
                                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                Toast.makeText(
                                    baseContext, "Authentication failed.", Toast.LENGTH_SHORT
                                ).show()

                                email?.setText("")
                                password?.setText("")
                                email.requestFocus()

                            }
                        }
                    }
            }
        }
    }
}