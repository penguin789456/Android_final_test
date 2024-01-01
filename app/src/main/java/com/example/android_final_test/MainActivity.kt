package com.example.android_final_test


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnDet = findViewById<Button>(R.id.btnDetail)
        val btnLink = findViewById<Button>(R.id.btnLink)

        /*val myName = arrayOf("XXX","OOO","QQQ","YYY","ZZZ")

        val bundle = Bundle()
        bundle.putStringArray("myDate",myName)

        val fragment = Fragment_advertise()
        fragment.arguments = bundle*/

        btnDet.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView3,Fragment_advertise())
                commit()
            }
        }



    }
}