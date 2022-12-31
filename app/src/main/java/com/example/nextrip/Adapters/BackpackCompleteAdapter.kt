package com.example.nextrip.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nextrip.R
import com.example.nextrip.model.ItemData

class BackpackCompleteAdapter(private val itemList: ArrayList<ItemData>): RecyclerView.Adapter<BackpackCompleteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_completed_backpack, parent, false)
        return BackpackCompleteAdapter.ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.isrented.text = currentItem.rented.toString()
        holder.name.text = currentItem.name.toString()
        holder.quantity.text = "Quantity : " + currentItem.quantity.toString()
        holder.description.text = currentItem.description.toString()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val isrented : TextView = itemView.findViewById(R.id.list_item_txt_show_isrented)
        val name : TextView = itemView.findViewById(R.id.list_item_txt_show_name)
        val quantity : TextView = itemView.findViewById(R.id.list_item_txt_show_quantity)
        val description : TextView = itemView.findViewById(R.id.list_item_txt_show_description)
    }
}