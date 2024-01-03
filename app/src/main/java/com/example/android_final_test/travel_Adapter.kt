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

class travel_Adapter(private val context: Context, private val dataList: List<Event>,private var mLocation:Location) : RecyclerView.Adapter<travel_Adapter.MyViewHolder>(){

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trave_name:TextView=itemView.findViewById(R.id.cardview_text)
        val travel_url:TextView=itemView.findViewById(R.id.travel_url)
        val travel_detail:TextView=itemView.findViewById(R.id.travel_detail)
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


        var origin:String=mLocation.latitude.toString()+","+mLocation.longitude.toString()
        holder.travel_url.setOnClickListener {
//            Log.d("intent","$origin,${currentItem.latitude},${currentItem.longitude}")
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/dir/?api=1&origin=$origin&destination=${currentItem.latitude},${currentItem.longitude}"))
            intent.setPackage("com.google.android.apps.maps")
            it.context.startActivity(intent)
        }

        holder.travel_detail.setOnClickListener {
//            Log.d("intent","$origin,${currentItem.latitude},${currentItem.longitude}")
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/dir/?api=1&origin=$origin&destination=${currentItem.latitude},${currentItem.longitude}"))
            intent.setPackage("com.google.android.apps.maps")
            it.context.startActivity(intent)
        }
    }


}