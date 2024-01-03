package com.example.android_final_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    val recyclerview = findViewById<RecyclerView>(R.id.RecyClerView)

        recyclerview.layoutManager = LinearLayoutManager(this)//通知管理者

        val Dataa = ArrayList<oneitem>()//從int到oneitem(因名字變成oneitem)
    for (i in 0..9) {
        Dataa.add(oneitem("station","information","Button $i","LinkButton $i"))
    }
        val adapter = recyclerviewAdapter(Dataa)//從上述Data放到這裡,但需要自訂義東西來去繼承
        recyclerview.adapter = adapter
    }
}