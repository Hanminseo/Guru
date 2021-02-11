package com.example.swu_2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.example.swu_2.R.layout.activity_start_logo
import java.util.jar.Manifest

class StartLogo : AppCompatActivity() {

    //보이는 시간 지정
    private val SPLASH_TIME_OUT : Long = 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_start_logo)

        //앱 시작 시 처음으로 보이고 넘어가도록 설정
        Handler().postDelayed({

            startActivity(Intent(this,MainActivity::class.java))

            finish()
        }, SPLASH_TIME_OUT)
    }
}