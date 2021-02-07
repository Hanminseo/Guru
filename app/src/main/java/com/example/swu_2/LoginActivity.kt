package com.example.swu_2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    private val TAG : String = "LoginActivity"
    lateinit var btnLogin : Button
    lateinit var tvReg : TextView
    lateinit var tvFind : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.edtId)
        val password = findViewById<EditText>(R.id.edtPasswd)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvReg = findViewById<TextView>(R.id.tvReg)
        val tvFind = findViewById<TextView>(R.id.tvFind)

        //로그인
        btnLogin.setOnClickListener{
            if(email.text.toString().length == 0 || password.text.toString().length==0) {
                Toast.makeText(this, "email 혹은 password를 입력하세요.", Toast.LENGTH_SHORT).show()
            }else{
                auth.signInWithEmailAndPassword(email.text.toString(),password.text.toString())
                    .addOnCompleteListener(this){ task ->
                        if(task.isSuccessful) {
                            startActivity(Intent(this, cal_test::class.java))
                            finish()

                            Log.d(TAG, "signInWithEmail:success")

                        }else{
                            Log.w(TAG,"signInWithEmail:failure", task.exception)
                            Toast.makeText(baseContext,"Authentication failed.",Toast.LENGTH_SHORT).show()

                        }
                }

            }

        }

        //비밀번호 찾기
        tvFind.setOnClickListener{
            val intent = Intent(this,Password::class.java)
            startActivity(intent)


        }


        //회원가입
        tvReg.setOnClickListener{
            val intent = Intent(this,Join::class.java)
            startActivity(intent)


        }

    }



}