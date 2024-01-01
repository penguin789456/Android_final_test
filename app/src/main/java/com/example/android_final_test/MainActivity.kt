package com.example.android_final_test

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.*
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {
    private var locationCode:Int=1001
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LocationGet()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

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
        }
    }

    private fun makeShowNotify(s:String){
        val notifyManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel= NotificationChannel("mCounter","Channel Counter",NotificationManager.IMPORTANCE_DEFAULT)
        notifyManager.createNotificationChannel(channel)
        val myBuilder= NotificationCompat.Builder(this,"mCounter").apply{
            setContentTitle("到期通知")
            setContentText("通知:$s")
            setSubText("我在狀態列")
            setWhen(System.currentTimeMillis())
            setChannelId("mCounter")
            setSmallIcon(R.drawable.penguin)
        }
        notifyManager.notify(1,myBuilder.build())
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == locationCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED&&grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                makeText(this, "取得權限", LENGTH_SHORT).show()
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location : Location? ->
                        if (location!=null){
                            makeShowNotify("${location.latitude},${location.longitude}")
                        }
                    }
            } else {
                makeText(this, "需要定位權限", LENGTH_SHORT).show()
            }
        }
    }


}