package com.example.android_final_test

import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    private lateinit var location:Location //宣告location
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mLocationManager = getSystemService(LOCATION_SERVICE) as LocationManager //取得location manager
    }
}