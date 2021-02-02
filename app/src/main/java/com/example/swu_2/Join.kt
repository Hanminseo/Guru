package com.example.swu_2

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Join : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    private val TAG : String = "Join"
    lateinit var Ok : Button
    //lateinit var tvReg : TextView

  //  @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.join)

        auth = FirebaseAuth.getInstance()

        //사용자가 입력한 값
        val email = findViewById<EditText>(R.id.Email)
        val password = findViewById<EditText>(R.id.Password)
        val btnOk = findViewById<Button>(R.id.btnOk)

        //계정만들기
        btnOk.setOnClickListener{
            if(email.text.toString().length == 0 || password.text.toString().length ==0){
                Toast.makeText(this, "email 혹은 password를 입력하세요.", Toast.LENGTH_SHORT).show()
            }else {
                auth.createUserWithEmailAndPassword(email.text.toString(),password.text.toString())
                    .addOnCompleteListener(this){ task ->
                        if(task.isSuccessful) {
                            Log.d(TAG, "createUserWithEmail:success")
                           // val user = auth.currentUser
                            finish()
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