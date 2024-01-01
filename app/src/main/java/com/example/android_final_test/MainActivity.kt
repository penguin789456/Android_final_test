package com.example.android_final_test

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import android.util.Log
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity(),LocationListener {
    //data class Event(val id:Long,val name: String, val longitude: Double, val latitude: Double,val address:String)  //創建Event資料型態 儲存活動位置

    private lateinit var mLocationManager: LocationManager      //LocationManager
    private var hasGPS:Boolean = false          //檢查是否有GPS
    private var eventDistance: Float? = null      //活動距離(公尺)
    private var isInEventRange:Boolean = false  //判斷是否有在範圍內
    private var nowLocation = Location("nowLocation")       //現在位置
    private var eventLocation = Location("eventLocation")   //活動位置
    private val dbHelper = DBHelper(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mLocationManager = getSystemService(LOCATION_SERVICE) as LocationManager    //取得location manager
        checkLocationPermission()  //檢查location權限
        readCSV2DataBase()

    }

    override fun onLocationChanged(p0: Location) {
        getNowLocation(p0.latitude,p0.longitude)                             //目前經緯度
        val nearestEvent = findNearestEvent()
        getEventLocation(nearestEvent!!.latitude,nearestEvent.longitude)     //活動經緯度
        eventDistance = nowLocation.distanceTo(eventLocation)           //取得與活動間的距離
        isInEventRange = inEventRange(eventDistance!!.toInt())          //判斷是否在活動範圍內

        Log.d("LTag","nowLongitude:${nowLocation.longitude} \nnowLatitude:${nowLocation.latitude} ")
        Log.d("LTag","eventName:${nearestEvent.name} \neventLongitude:${nearestEvent.longitude} \neventLatitude:${nearestEvent.latitude}  \n" +
                "Distance:${eventDistance}m \ninEventRange:${isInEventRange}")
    }

    //取得現在位置
    private fun getNowLocation(latitude: Double, longitude: Double) {
        nowLocation.longitude = longitude
        nowLocation.latitude = latitude
    }

    //取得活動位置
    private fun getEventLocation(latitude: Double, longitude: Double) {
        eventLocation.longitude = longitude
        eventLocation.latitude = latitude
    }

    //判斷是否進入活動範圍
    private fun inEventRange(distance:Int): Boolean {
        //距離活動小於50m 回傳true
        return distance < 50
    }

    //找出最近的活動
    private fun findNearestEvent(): Event? {
        var nearestEvent: Event? = null
        var minDistance = Float.MAX_VALUE
        var events = readEventsFromDatabase() //改由sql匯入 readEventsFromDatabase()

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

    //檢查location權限
    private fun checkLocationPermission(){
        hasGPS = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) //檢查GPS是否啟用
        if(hasGPS){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),1)
            }
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1.0f,this)  //取得位置(1秒更新,移動1m更新)
        }else{
            Log.d("LTag","設備未提供定位服務")  //如果沒找到GPS
        }
    }

    //暫時的，之後改由slq匯入list(用cursor)
    private fun readEventsFromDatabase(): List<Event> {
        return  listOf(
            Event(1123L, "event1", "1111", "1010", -122.080816, 37.421306),
        )
    }

    private fun readCSV2DataBase(){
        val inputStream: InputStream = resources.openRawResource(R.raw.acd4f2fa19cd40ed)
        val reader = BufferedReader(InputStreamReader(inputStream))


        reader.readLine()
        var line: String?
        while (reader.readLine().also { line = it } != null) {

            val data = line?.split(",")
            val id = data?.get(0)?.toLong() ?: 0L
            val name:String = data?.get(1) ?: ""
            val tel:String = data?.get(2) ?: ""
            val address:String = data?.get(3) ?: ""
            val longitude:Double = data?.get(4)?.toDouble() ?: 0.0
            val latitude:Double = data?.get(5)?.toDouble() ?: 0.0
            var event:Event = Event(id,name,tel,address,longitude,latitude)
            dbHelper.insertEvent(event)

            Log.d("LTag","${event.id}:${event.name}")
        }
        reader.close()
    }
    override fun onResume() {
        super.onResume()
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)  //重新取得location
    }

    override fun onPause() {
        super.onPause()
        mLocationManager.removeUpdates(this)  //取消location更新
    }
}


