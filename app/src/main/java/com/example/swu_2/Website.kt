package com.example.swu_2

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class Website : AppCompatActivity() {

    lateinit var allcon : ImageButton
    lateinit var specup : ImageButton
    lateinit var linkareer : ImageButton
    lateinit var wevity : ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_website)

        allcon = findViewById(R.id.allcon)
        specup = findViewById(R.id.specup)
        linkareer = findViewById(R.id.linkareer)
        wevity = findViewById(R.id.wevity)

        allcon.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.all-con.co.kr"))
            startActivity(intent)
        }

        specup.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://cafe.naver.com/specup"))
            startActivity(intent)
        }

        linkareer.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkareer.com/"))
            startActivity(intent)
        }

        wevity.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.wevity.com/"))
            startActivity(intent)
        }

    }
}