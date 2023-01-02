package com.example.nextrip.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nextrip.R
import com.example.nextrip.model.EmergancyData

class EmeragancyAdapter (private val msgList: ArrayList<EmergancyData>): RecyclerView.Adapter<EmeragancyAdapter.ViewHolder>() {

    private lateinit var msgListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setonItemClickListener(clickListener: onItemClickListener){
        msgListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_em_users, parent, false)
        return ViewHolder(itemView, msgListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = msgList[position]
        holder.userid.text = currentItem.userid.toString()
        holder.msg.text = currentItem.msg.toString()
        holder.latitude.text = "latitude : " + currentItem.latitude.toString()
        holder.longitude.text = "longitude : " + currentItem.longitude.toString()
        holder.locationdetails.text = currentItem.locationdetails.toString()
        holder.date.text = currentItem.date.toString()
        holder.time.text = currentItem.time.toString()
    }

    override fun getItemCount(): Int {
        return msgList.size
    }

    class ViewHolder(view: View, clickListener: onItemClickListener): RecyclerView.ViewHolder(view) {
        val userid : TextView = itemView.findViewById(R.id.list_em_show_userid)
        val msg : TextView = itemView.findViewById(R.id.list_em_show_msg)
        val latitude : TextView = itemView.findViewById(R.id.list_em_show_latitude)
        val longitude : TextView = itemView.findViewById(R.id.list_em_show_longitude)
        val locationdetails : TextView = itemView.findViewById(R.id.list_em_show_locationdetails)
        val date : TextView = itemView.findViewById(R.id.list_em_show_date)
        val time : TextView = itemView.findViewById(R.id.list_em_show_time)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }
}