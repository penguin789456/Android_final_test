package com.example.android_final_test

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class travel_Adapter(private val context: Context, private val dataList: List<Event>) : RecyclerView.Adapter<travel_Adapter.MyViewHolder>(){

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trave_name:TextView=itemView.findViewById(R.id.cardview_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.travel_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d("dataList.size",dataList.size.toString())
        return  dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.trave_name.text=currentItem.name
    }



}