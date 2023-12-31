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

    private var hasGPS:Boolean = false          //檢查是否有GPS
    private lateinit var mLocationManager: LocationManager   //LocationManager
    private var nowLocation = Location("nowLocation")  //現在位置
    private var eventLocation = Location("eventLocation") //活動位置
    private  var eventDistance: Int? = null  //活動距離(公尺)

    //test 可刪
    private lateinit var longitude: TextView // 經度
    private lateinit var latitude: TextView  // 緯度
    private lateinit var tvDistance: TextView
    //test
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mLocationManager = getSystemService(LOCATION_SERVICE) as LocationManager    //取得location manager
        checkLocationPermission()  //檢查location權限



        //test
        longitude = findViewById(R.id.testTV)
        latitude = findViewById(R.id.testTV2)
        tvDistance = findViewById(R.id.tvDistance)
        //test
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
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0.01f,this)  //取得位置(2秒更新,移動1m更新)
        }else{
                Log.d("LTag","設備未提供定位服務")  //如果沒找到GPS
            }
    }

    override fun onLocationChanged(p0: Location) {
        //目前經緯度
        nowLocation.longitude = p0.longitude
        nowLocation.latitude = p0.latitude
        //活動經緯度
        eventLocation.longitude =  -122.080816  //之後改為由sql匯入Event經緯度(用function回傳)
        eventLocation.latitude = 37.421306

        getDistanceToEvent( eventLocation.longitude,eventLocation.latitude)
        //test
        longitude.text = nowLocation.longitude.toString()
        latitude.text = nowLocation.latitude.toString()
        Log.d("LTag","longitude${nowLocation.longitude},latitude${nowLocation.latitude},distance:${eventDistance}")
        //test
    }

    //取得與活動間的距離
    private fun getDistanceToEvent(longitude:Double,latitude:Double) {
        //計算距離
        eventDistance = nowLocation.distanceTo(eventLocation).toInt()
        tvDistance.text = eventDistance.toString()
    }

    //取得活動位置
    private fun getEventLocation(){
        //透過sql回傳所有經緯度，然後使用透過比大小，將距離我們最近的活動訊息傳回來。
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