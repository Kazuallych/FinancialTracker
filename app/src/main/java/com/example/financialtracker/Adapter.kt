package com.example.financialtracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView


class Adapter( var data: ArrayList<Item>): RecyclerView.Adapter<Adapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var tvText = view.findViewById<TextView>(R.id.tvText)
        var btDel = view.findViewById<Button>(R.id.btDel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shablon_activity,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[holder.bindingAdapterPosition]

        if(item.transaction){
            holder.tvText.setTextColor("#00e600".toColorInt())
        }
        else{
            item.number -= item.number*2
            holder.tvText.setTextColor("#FF0000".toColorInt())
        }

        holder.tvText.text = item.number.toString()

        holder.btDel.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}