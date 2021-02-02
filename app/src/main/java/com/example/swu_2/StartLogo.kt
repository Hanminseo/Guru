package com.example.swu_2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class StartLogo : AppCompatActivity() {

    private val SPLASH_TIME_OUT : Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_logo)

        Handler().postDelayed({

            startActivity(Intent(this, Website::class.java))

            finish()
        }, SPLASH_TIME_OUT)
    }
}