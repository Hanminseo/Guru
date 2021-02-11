package com.example.swu_2

import android.content.Intent
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

        //메일전송
        btnSend.setOnClickListener {

            //공백시 오류메세지
            if (PwdFind.text.toString().length == 0) {
                Toast.makeText(this, "email을 입력하세요.", Toast.LENGTH_SHORT).show()
            } else {
                //firebase에 있는 이메일인지 확인
                FirebaseAuth.getInstance().sendPasswordResetEmail(PwdFind?.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            //인증시 해당 이메일로 비밀번호 변경 메일 전송
                            Toast.makeText(this, "메일을 전송했습니다", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        } else {
                            //인증실패시 오류메세지
                            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}

