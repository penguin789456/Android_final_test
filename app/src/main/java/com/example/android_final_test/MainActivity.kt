package com.example.android_final_test

import android.Manifest
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {
    private lateinit var btn1: Button
    private lateinit var location: Location
    private lateinit var mLocationManager:LocationManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mLocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        Log.d("location",LocationGet().toString())


        var latitude=25.03655
        var longitude=121.86594
        btn1=findViewById(R.id.button1)

        // 替換成您的CSV檔案URL
        btn1.setOnClickListener { Google_Map(latitude,longitude,"") }

    }

    private fun downloadFile(url: String) {
        val uri = Uri.parse(url)

        val request = DownloadManager.Request(uri)
            .setTitle(uri.lastPathSegment)  // 使用原始檔案名稱
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uri.lastPathSegment)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
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
                ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),1001)
            }else{
                location= mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!
            }
        location
    }

    private fun Google_Map(latitude:Double,longitude:Double,destination:String) {
        var getLocation:Location
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->}.also { getLocation=location }

        val origin = getLocation.latitude.toString()+","+getLocation.longitude // 例如：String origin = "40.7128,-74.0060";

        val destination = latitude.toString()+","+longitude // 例如：String destination = "34.0522,-118.2437";

        val url ="https://www.google.com/maps/dir/?api=1&origin=$origin&destination=$destination"

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }
}