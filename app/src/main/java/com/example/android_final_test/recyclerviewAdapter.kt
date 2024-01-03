package com.example.android_final_test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class recyclerviewAdapter(val informationList:ArrayList<oneitem>) :RecyclerView.Adapter<recyclerviewAdapter.ViewHolder> (){
    class ViewHolder(ItemView: View) :RecyclerView.ViewHolder(ItemView){
        val VHnumber:TextView = itemView.findViewById(R.id.mnumber)//呼叫mnumber給VHnumber
        val VHinformation:TextView = itemView.findViewById(R.id.minformation)
        val VHdetail:Button = itemView.findViewById(R.id.mdetail)
        val VHlink:Button = itemView.findViewById(R.id.mlink)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {//樣式
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview,parent,false)//不知道從哪傳進來(從parent傳進來的)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {//回應外界的問的(取得外部的東西)
        return informationList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val oneItem = informationList[position]//抓取位置(自動)
            holder.VHnumber.text = oneItem.number
            holder.VHinformation.text = oneItem.information
        holder.VHdetail.text = "Hardcoded Detail"
        holder.VHlink.text = "Hardcoded Link"
    }

//mlist = informationlist                                                                                   //自己定義ViewHolder

}