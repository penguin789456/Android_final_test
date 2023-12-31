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

class MainActivity : AppCompatActivity(),LocationListener {

    private var hasGPS:Boolean = false          //檢查是否有GPS
    private lateinit var mLocationManager: LocationManager   //宣告LocationManager



    //test 可刪
    private lateinit var longitude: TextView // 經度
    private lateinit var latitude: TextView  // 緯度
    //test
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mLocationManager = getSystemService(LOCATION_SERVICE) as LocationManager    //取得location manager
        checkLocationPermission()  //檢查location權限



        //test
        longitude = findViewById(R.id.testTV)
        latitude = findViewById(R.id.testTV2)
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