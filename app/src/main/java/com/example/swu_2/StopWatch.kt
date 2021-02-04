package com.example.swu_2

import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.timer

class StopWatch : AppCompatActivity(){

    var time = 0
    var timerTask :Timer? =null
    var isRunning = false
    var lap = 1

    lateinit var playbtn : ImageButton
    lateinit var stopwatch_hour : TextView
    lateinit var stopwatch_min : TextView
    lateinit var subjectName : TextView
    lateinit var datestring : TextView
    lateinit var you_study : TextView
    lateinit var i_study : TextView
    lateinit var you_studytime : TextView
    lateinit var i_studytime : TextView
    lateinit var upload_list_btn : Button

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_stopwatchplay)

        playbtn = findViewById(R.id.playbtn)
        stopwatch_hour = findViewById(R.id.stopwatch_hour)
        stopwatch_min = findViewById(R.id.stopwatch_min)
        subjectName = findViewById(R.id.subjectName)
        datestring = findViewById(R.id.datestring)
        you_study = findViewById(R.id.you_study)
        i_study = findViewById(R.id.i_study)
        you_studytime = findViewById(R.id.you_studytime)
        i_studytime = findViewById(R.id.i_studytime)
        upload_list_btn = findViewById(R.id.upload_list_btn)

        playbtn.setOnClickListener {
            isRunning =!isRunning

            if(isRunning){
                start()
            }else{
                pause()
            }
        }

        i_studytime.text = "0 : 0"
        subjectName.text = i_study.text

        var now = LocalDate.now()

        var Strnow = now.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        datestring.text = Strnow

    }

    //시작 버튼 눌렀을때
    private fun start(){
        playbtn.setImageResource(R.drawable.ic_noun_pause_2494384)

        timerTask = timer(period=60000){
            time++
            val hour = time /600000
            val min = time % 600000

            runOnUiThread{
                stopwatch_hour.text = "$hour"
                stopwatch_min.text = "$min"
            }
        }
    }

    //멈춤 버튼 눌렀을때

    private fun pause(){
        playbtn.setImageResource(R.drawable.ic_noun_play_943866)
        timerTask?.cancel()

        val lapTime = this.time
        val textView = TextView(this)
        i_studytime.text = "${lapTime/600000}:${lapTime%600000}"
    }

}