package com.example.android_final_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    val recyclerview = findViewById<RecyclerView>(R.id.RecyClerView)


        val Dataa = ArrayList<oneitem>()//從int到oneitem(因名字變成oneitem)

        val adapter = recyclerviewAdapter(Dataa)//從上述Data放到這裡,但需要自訂義東西來去繼承
    }
}