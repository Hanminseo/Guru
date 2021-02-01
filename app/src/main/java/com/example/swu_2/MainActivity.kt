package com.example.swu_2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.CalendarView
import android.widget.ImageButton
import android.widget.TextView
import com.dinuscxj.progressbar.CircleProgressBar
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    CircleProgressBar.ProgressFormatter {

    //변수
    lateinit var userID : TextView
    lateinit var userIDcheck : TextView
    lateinit var userPage : ImageButton
    lateinit var mainHome : ImageButton
    lateinit var myCalendar : CalendarView
    lateinit var listEdit : ImageButton

    // 원형 프로그레스 바 설정 변수
    private val DEFAULT_PATTERN = "%d%%"
    lateinit var circleProgressBar: CircleProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userID = findViewById(R.id.userID)
        userIDcheck = findViewById(R.id.userIDcheck)
        userPage = findViewById(R.id.userPage)
        mainHome = findViewById(R.id.mainHome)
        myCalendar = findViewById(R.id.myCalendar)
        listEdit = findViewById(R.id.listEdit)

        val bottomNavigationView = findViewById<View>(R.id.bottom_navigation) as BottomNavigationView

        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        circleProgressBar=findViewById(R.id.cpb_circlebar);
        circleProgressBar.setProgress(70);  // 해당 퍼센트를 적용

        listEdit.setOnClickListener {
            val intent = Intent(this, TodoList::class.java)
            startActivity(intent)
        }


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_search -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_container, Frag01())
                    .commit()
                return true
            }
            R.id.nav_settings -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_container, Frag01())
                    .commit()
                return true
            }
            R.id.nav_navigation -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_container, Frag01())
                    .commit()
                return true
            }
        }
        return false
    }

    override fun format(progress: Int, max: Int): CharSequence? {
        return String.format(
            DEFAULT_PATTERN,
            (progress.toFloat() / max.toFloat() * 100).toInt()
        )
    }
}