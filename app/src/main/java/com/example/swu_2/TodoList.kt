package com.example.swu_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class TodoList : AppCompatActivity() {

    lateinit var listPlus : ImageButton
    lateinit var listDelete : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)

        listPlus = findViewById(R.id.listPlus)
        listDelete = findViewById(R.id.listDelete)

        listPlus.setOnClickListener {

        }
        listDelete.setOnClickListener {

        }
    }
}