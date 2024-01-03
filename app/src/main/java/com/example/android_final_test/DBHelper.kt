package com.example.android_final_test

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.location.Location
import android.util.Log

data class Event(var id:Long, var name: String, var tel: String, var address: String, var longitude: Double, var latitude: Double)
private lateinit  var main:MainActivity

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "DBEvent"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "event"
        const val COLUMN_ID = "Id"
        const val COLUMN_NAME = "Name"
        const val COLUMN_TEL = "Tel"
        const val COLUMN_ADDRESS = "Address"
        const val COLUMN_PX = "Px"
        const val COLUMN_PY = "Py"

        const val CREATE_TABLE_QUERY = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY,
                $COLUMN_NAME TEXT,
                $COLUMN_TEL TEXT,
                $COLUMN_ADDRESS TEXT,
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
        try {
            val values = ContentValues().apply {
                put(COLUMN_ID, event.id)
                put(COLUMN_NAME, event.name)
                put(COLUMN_TEL, event.tel)
                put(COLUMN_ADDRESS, event.address)
                put(COLUMN_PX, event.longitude)
                put(COLUMN_PY, event.latitude)
            }
            val newRowId = writableDatabase.insert(TABLE_NAME, null, values)

            if (newRowId != -1L) {
                Log.d("LTag", "Event inserted successfully. Row ID: $newRowId")
            } else {
                Log.wtf("LTag", "Failed to insert event.")
            }
        } catch (e: Exception) {
            Log.e("LTag", "Insertion failed", e)
        }
    }

    fun queryNearestEvents(nowLocation:Location): Event? {
        val events = mutableListOf<Event>() //全部Events
        var cursor: Cursor? = null

        try {
            cursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME", null)
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
                    val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                    val tel = cursor.getString(cursor.getColumnIndex(COLUMN_TEL))
                    val address = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS))
                    val longitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_PX))
                    val latitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_PY))

                    val event = Event(id, name, tel, address, longitude, latitude) //單一Event
                    events.add(event)
                } while (cursor.moveToNext())
            } else {
                Log.d("LTag", "No events found in the database")
            }
        } catch (e: Exception) {
            Log.e("LTag", "Query failed", e)
        } finally {
            cursor?.close()
            Log.d("LTag", "Found cursor")
        }
        return findNearestEvent(nowLocation,events)
    }
    //將所有event資料回傳
    fun queryAllEvents(nowLocation:Location): List<Event> {
        val events = mutableListOf<Event>() //全部Events
        var cursor: Cursor? = null

        try {
            cursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME", null)
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
                    val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                    val tel = cursor.getString(cursor.getColumnIndex(COLUMN_TEL))
                    val address = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS))
                    val longitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_PX))
                    val latitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_PY))

                    val event = Event(id, name, tel, address, longitude, latitude) //單一Event
                    events.add(event)
                } while (cursor.moveToNext())
            } else {
                Log.d("LTag", "No events found in the database")
            }
        } catch (e: Exception) {
            Log.e("LTag", "Query failed", e)
        } finally {
            cursor?.close()
            Log.d("LTag", "Found cursor")
        }
        return events
    }
    //找出最近的活動
    private fun findNearestEvent(nowLocation: Location,events: List<Event>): Event? {
        var nearestEvent: Event? = null
        var minDistance = Float.MAX_VALUE

        for(event in events){
            val location = Location("event")
            location.latitude = event.latitude
            location.longitude = event.longitude

            val distance = nowLocation.distanceTo(location)
            if(distance < minDistance){
                minDistance = distance
                nearestEvent = event
            }
        }
        return nearestEvent
    }
}

