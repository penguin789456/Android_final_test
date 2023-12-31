package com.example.android_final_test

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {
    private lateinit var btn1: Button
    private lateinit var location: Location
    private var locationCode:Int=1001
    private lateinit var mLocationManager:LocationManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mLocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        LocationGet()
        var latitude=25.03655
        var longitude=121.86594
        btn1=findViewById(R.id.button1)

        // 替換成您的CSV檔案URL
        btn1.setOnClickListener { Google_Map(latitude,longitude,"") }

    }

    private fun LocationGet() {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),locationCode)
            }else {
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!
            }
    }


    @SuppressLint("MissingPermission")
    private fun Google_Map(latitude:Double,longitude:Double,destination:String) {
        var getLocation:Location
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->}.also { getLocation=location }

        val origin = getLocation.latitude.toString()+","+getLocation.longitude // 例如：String origin = "40.7128,-74.0060";

        val destination = latitude.toString()+","+longitude // 例如：String destination = "34.0522,-118.2437";

        val url ="https://www.google.com/maps/dir/?api=1&origin=$origin&destination=$destination"

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }
    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == locationCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED&&grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "取得權限", Toast.LENGTH_SHORT).show()
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!
            } else {
                Toast.makeText(this, "需要定位權限", Toast.LENGTH_SHORT).show()
            }
        }
    }

}