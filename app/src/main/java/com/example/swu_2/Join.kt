package com.example.swu_2

import android.annotation.SuppressLint
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
    //  lateinit var btnOk : Button
    //lateinit var tvReg : TextView

    var emailAuth : FirebaseAuth? = null
    var emailFirestore : FirebaseFirestore? = null

    //  @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.join)

        auth = FirebaseAuth.getInstance()

        //firestore
        emailAuth = FirebaseAuth.getInstance()
        emailFirestore = FirebaseFirestore.getInstance()


        //사용자가 입력한 값
        val email = findViewById<EditText>(R.id.Email)
        val password = findViewById<EditText>(R.id.Password)
        val btnOk = findViewById<Button>(R.id.btnOk)
        val name = findViewById<EditText>(R.id.Name)
        val birthday = findViewById<EditText>(R.id.Birthday)
        val phonenum = findViewById<EditText>(R.id.PhoneNum)

        //계정만들기
        btnOk.setOnClickListener{
            if(email.text.toString().length == 0 || password.text.toString().length ==0){
                Toast.makeText(this, "email 혹은 password를 입력하세요.", Toast.LENGTH_SHORT).show()
            }

            else {
                auth.createUserWithEmailAndPassword(email.text.toString(),password.text.toString())
                    .addOnCompleteListener(this){ task ->
                        if(task.isSuccessful) {
                            Log.d(TAG, "createUserWithEmail:success")
                            // val user = auth.currentUser
                            finish()

                            //firestore 저장
                            if(true){
                                var userInfo = Member()
                                var groupInfo  = group_m()

                                userInfo.storeUid = emailAuth?.uid
                                userInfo.storeEmail=emailAuth?.currentUser?.email
                                userInfo.storeBirth=birthday.getText().toString()
                                userInfo.storeName=name.getText().toString()
                                userInfo.storePhone=phonenum.getText().toString()

                                groupInfo.storeContent="null"


                                emailFirestore?.collection("member")?.document(emailAuth?.uid.toString())?.set(userInfo)

                                emailFirestore?.collection("group")?.document(phonenum.getText().toString())?.set(groupInfo)
                            }
                        }else{
                            Log.w(TAG,"createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,"Authentication failed.",Toast.LENGTH_SHORT).show()

                            email?.setText("")
                            password?.setText("")
                            email.requestFocus()

                        }
                    }
            }
        }
    }
}