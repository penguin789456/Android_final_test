package com.example.android_final_test

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.core.app.ActivityCompat
import org.w3c.dom.Text

class MainActivity : AppCompatActivity(),LocationListener {

    private lateinit var mLocationManager: LocationManager      //LocationManager
    private var hasGPS:Boolean = false          //檢查是否有GPS
    private var eventDistance: Int? = null      //活動距離(公尺)
    private var isInEventRange:Boolean = false  //判斷是否有在範圍內
    private var nowLocation = Location("nowLocation")       //現在位置
    private var eventLocation = Location("eventLocation")   //活動位置
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mLocationManager = getSystemService(LOCATION_SERVICE) as LocationManager    //取得location manager
        checkLocationPermission()  //檢查location權限
    }

    override fun onLocationChanged(p0: Location) {
        //目前經緯度
        nowLocation.longitude = p0.longitude
        nowLocation.latitude = p0.latitude
        //活動經緯度
        eventLocation.longitude =  -122.080816  //之後改為由sql匯入Event經緯度(用function回傳)
        eventLocation.latitude = 37.421306

        getDistanceToEvent( eventLocation.longitude,eventLocation.latitude)  //距離在eventDistance
        isInEventRange = inEventRange(eventDistance!!)  //判斷是否在活動範圍內

        Log.d("LTag","longitude${nowLocation.longitude},latitude${nowLocation.latitude} \n inEventRange:${isInEventRange}")
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

    //取得與活動間的距離
    private fun getDistanceToEvent(longitude:Double,latitude:Double) {
        //計算距離
        eventDistance = nowLocation.distanceTo(eventLocation).toInt()
        Log.d("LTag","eventDistance:${eventDistance}")
    }

    //判斷是否進入活動範圍
    private fun inEventRange(distance:Int): Boolean {
        //距離活動小於50m 回傳true
        return distance < 50
    }

    //取得活動位置
    private fun getEventLocation(){
        //透過sql回傳所有經緯度，然後使用透過比大小，將距離我們最近的活動訊息傳回來。
        //sql 
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