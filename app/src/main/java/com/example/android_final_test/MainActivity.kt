package com.example.android_final_test


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val btnLink = findViewById<Button>(R.id.btnLink)

        /*val AName = arrayOf("XXX","OOO","QQQ","YYY","ZZZ")
        val AContent = arrayOf("111","222","333","444","555")

        val bundle = Bundle()
        bundle.putStringArray("myDate",myName)

        val fragment = Fragment_advertise()
        fragment.arguments = bundle*/





    }
}