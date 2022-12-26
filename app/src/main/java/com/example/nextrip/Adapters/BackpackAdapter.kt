package com.example.nextrip.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nextrip.R
import com.example.nextrip.model.ItemData

class BackpackAdapter (private val itemList: ArrayList<ItemData>): RecyclerView.Adapter<BackpackAdapter.ViewHolder>(){

    private lateinit var itemListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setonItemClickListener(clickListener: onItemClickListener){
        itemListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_backpack, parent, false)
        return ViewHolder(itemView, itemListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.name.text = currentItem.name.toString()
        holder.quantity.text = "Quantity : " + currentItem.quantity.toString()
        holder.isRented.text = currentItem.rented.toString()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(view: View, clickListener: onItemClickListener): RecyclerView.ViewHolder(view) {
        val name : TextView = itemView.findViewById(R.id.list_item_show_name)
        val quantity : TextView = itemView.findViewById(R.id.list_item_show_quantity)
        val isRented : TextView = itemView.findViewById(R.id.list_item_show_isRentedItem)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }
}
