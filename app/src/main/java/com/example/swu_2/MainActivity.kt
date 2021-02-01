package com.example.swu_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CalendarView
import android.widget.ImageButton
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    lateinit var userID : TextView
    lateinit var userIDcheck : TextView
    lateinit var userPage : ImageButton
    lateinit var mainHome : ImageButton
    lateinit var myCalendar : CalendarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userID = findViewById(R.id.userID)
        userIDcheck = findViewById(R.id.userIDcheck)
        userPage = findViewById(R.id.userPage)
        mainHome = findViewById(R.id.mainHome)
        myCalendar = findViewById(R.id.myCalendar)    }
}