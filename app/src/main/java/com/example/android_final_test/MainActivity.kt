package com.example.android_final_test

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var btn1: Button
    private lateinit var location: Location
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mLocationManager = getSystemService(LOCATION_SERVICE) as LocationManager

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

    private fun Google_Map(latitude:Double,longitude:Double,destination:String) {
        val origin = "Shuangxi Station" // 例如：String origin = "40.7128,-74.0060";

        val destination = latitude.toString()+","+longitude // 例如：String destination = "34.0522,-118.2437";

        val url ="https://www.google.com/maps/dir/?api=1&origin=$origin&destination=$destination"

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }
}