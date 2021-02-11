package com.example.swu_2

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBManager(
    context: Context?,
    name: String?, // DB 생성 명
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE todolistTBL (item text, checked text);") // itemTBL 테이블 생성
        db!!.execSQL("CREATE TABLE stopwatchTBL (subject text, time text, targetTime text);") // stopwatchTBL 테이블 생성
        db!!.execSQL("CREATE TABLE calTBL (date text, content text, sign text);") // itemTBL 테이블 생성
        db!!.execSQL("CREATE TABLE noticeTBL ( notice text);") // noticeTBL 테이블 생성
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
    }
}