package com.example.moodtracker

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import com.example.moodtracker.data.Event
import com.example.moodtracker.data.Mood

class DBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createQuery =
            "CREATE TABLE ${CalenderData.EventEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${CalenderData.EventEntry.COLUMN_NAME_DATE} TEXT," +
                    "${CalenderData.EventEntry.COLUMN_NAME_EVENT_TITLE} TEXT,"  +
                    "${CalenderData.EventEntry.COLUMN_NAME_MOOD} INTEGER)"
        db?.execSQL(createQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val deleteQuery = "DROP TABLE IF EXISTS ${CalenderData.EventEntry.TABLE_NAME}"
        db?.execSQL(deleteQuery)
        onCreate(db)
    }

    fun addEvent(date: String, event: Event) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(CalenderData.EventEntry.COLUMN_NAME_DATE, date)
            put(CalenderData.EventEntry.COLUMN_NAME_EVENT_TITLE, event.name)
            put(CalenderData.EventEntry.COLUMN_NAME_MOOD, event.mood.ordinal)
        }
        db?.insert(CalenderData.EventEntry.TABLE_NAME, null, values)
    }

    fun fetchAllMoods(): List<Mood> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM " + CalenderData.EventEntry.TABLE_NAME, null)

        val moods = mutableListOf<Mood>()
        if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                moods.add(
                    Mood.values()[cursor.getInt(3)]
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return moods
    }

    fun fetchEventsFromDate(date: String): List<Event> {
        val db = this.readableDatabase
        val projection = arrayOf(
            BaseColumns._ID,
            CalenderData.EventEntry.COLUMN_NAME_DATE,
            CalenderData.EventEntry.COLUMN_NAME_EVENT_TITLE,
            CalenderData.EventEntry.COLUMN_NAME_MOOD
        )
        val selection = "${CalenderData.EventEntry.COLUMN_NAME_DATE} = ?"
        val selectionArgs = arrayOf(date)
        val sortOrder = "${CalenderData.EventEntry.COLUMN_NAME_EVENT_TITLE} DESC"
        val cursor = db.query(
            CalenderData.EventEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )
        val events = mutableListOf<Event>()
        if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                events.add(
                    Event(
                        cursor.getString(2),
                        Mood.values()[cursor.getInt(3)]
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return events
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
