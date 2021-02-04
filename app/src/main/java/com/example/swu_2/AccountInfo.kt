package com.example.swu_2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AccountInfo : AppCompatActivity() {

    lateinit var pswdbtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_info)

        pswdbtn = findViewById(R.id.pswdbtn)
        pswdbtn.setOnClickListener {
            startActivity(Intent(this,Password::class.java))
        }
    }
}