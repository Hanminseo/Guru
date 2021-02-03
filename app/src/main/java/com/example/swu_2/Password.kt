package com.example.swu_2


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class Password : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.password)

        val btnSend = findViewById<Button>(R.id.btnSend)
        val PwdFind = findViewById<EditText>(R.id.PwdFind)

        btnSend.setOnClickListener {

            //메일전송
            if (PwdFind.text.toString().length == 0) {
                Toast.makeText(this, "email을 입력하세요.", Toast.LENGTH_SHORT).show()
            } else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(PwdFind?.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "메일을 전송했습니다", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }


    }




}

