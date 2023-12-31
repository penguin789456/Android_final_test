package com.example.android_final_test

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        makeShowNotify("set one")
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

}