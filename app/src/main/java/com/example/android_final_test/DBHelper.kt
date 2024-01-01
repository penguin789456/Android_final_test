package com.example.android_final_test

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Event(val id:Long,val name: String, val tel: String, val address: String, val longitude: Double, val latitude: Double)

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "DBEvents"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "events"
        const val COLUMN_ID = "Id"
        const val COLUMN_NAME = "Name"
        const val COLUMN_TEL = "Tel"
        const val COLUMN_ADD = "Add"
        const val COLUMN_PX = "Px"
        const val COLUMN_PY = "Py"

        const val CREATE_TABLE_QUERY = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY,
                $COLUMN_NAME TEXT,
                $COLUMN_TEL TEXT,
                '$COLUMN_ADD' TEXT,
                $COLUMN_PX REAL,
                $COLUMN_PY REAL
            )
        """
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    fun insertEvent(event: Event) {
        val values = ContentValues().apply {
            put(COLUMN_ID, event.id)
            put(COLUMN_NAME, event.name)
            put(COLUMN_TEL, event.tel)
            put(COLUMN_ADD, event.address)
            put(COLUMN_PX, event.longitude)
            put(COLUMN_PY, event.latitude)
        }

        writableDatabase.insert(TABLE_NAME, null, values)
    }

    fun queryAllEvents(): List<Event> {
        val events = mutableListOf<Event>()
        val cursor: Cursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME", null)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
            val tel = cursor.getString(cursor.getColumnIndex(COLUMN_TEL))
            val address = cursor.getString(cursor.getColumnIndex(COLUMN_ADD))
            val longitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_PX))
            val latitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_PY))

            val event = Event(id, name, tel, address, longitude, latitude)
            events.add(event)
        }
        cursor.close()
        return events
    }
}
