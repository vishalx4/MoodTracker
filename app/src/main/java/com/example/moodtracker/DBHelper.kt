package com.example.moodtracker

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class DBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createQuery =
            "CREATE TABLE ${CalenderData.EventEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${CalenderData.EventEntry.COLUMN_NAME_DATE} TEXT," +
                    "${CalenderData.EventEntry.COLUMN_NAME_EVENT_TITLE} TEXT)" +
                    "${CalenderData.EventEntry.COLUMN_NAME_MOOD} INTEGER"
        db?.execSQL(createQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val deleteQuery = "DROP TABLE IF EXISTS ${CalenderData.EventEntry.TABLE_NAME}"
        db?.execSQL(deleteQuery)
        onCreate(db)
    }

    fun addEvent(date: String, eventTitle: String, mood: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(CalenderData.EventEntry.COLUMN_NAME_DATE, date)
            put(CalenderData.EventEntry.COLUMN_NAME_EVENT_TITLE, eventTitle)
            put(CalenderData.EventEntry.COLUMN_NAME_MOOD, mood)
        }
        db?.insert(CalenderData.EventEntry.TABLE_NAME, null, values)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "FeedReader.db"
    }
}

object CalenderData {
    object EventEntry : BaseColumns {
        const val TABLE_NAME = "calender"
        const val COLUMN_NAME_DATE = "date"
        const val COLUMN_NAME_EVENT_TITLE = "event"
        const val COLUMN_NAME_MOOD = "mood"
    }
}
