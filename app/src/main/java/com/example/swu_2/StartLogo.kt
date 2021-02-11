package com.example.swu_2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.swu_2.R.layout.activity_start_logo

class StartLogo : AppCompatActivity() {

    //보이는 시간 지정
    private val SPLASH_TIME_OUT : Long = 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_start_logo)

        //앱 시작 시 처음으로 보이고 넘어가도록 설정
        Handler().postDelayed({

            startActivity(Intent(this,LoginActivity::class.java))

            finish()
        }, SPLASH_TIME_OUT)
    }
}