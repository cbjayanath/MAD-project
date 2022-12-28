package com.example.nextrip.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nextrip.Location
import com.example.nextrip.R
import com.example.nextrip.model.LocationData

class LocationAdapter (private val locationList: ArrayList<LocationData>): RecyclerView.Adapter<LocationAdapter.ViewHolder>(){

    private lateinit var locationListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setonItemClickListener(clickListener: onItemClickListener){
        locationListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_location, parent, false)
        return ViewHolder(itemView, locationListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentLocation = locationList[position]
        holder.name.text = currentLocation.name.toString()
        holder.city.text = currentLocation.city.toString() + " city"
        holder.district.text = "in " + currentLocation.district.toString() + " district"
        holder.description.text = currentLocation.description.toString()
        holder.arrivaldate.text = currentLocation.arrivaldate.toString()
        holder.arrivaltime.text = currentLocation.addedTime.toString()
        holder.complete.text = currentLocation.complete.toString()
    }

    override fun getItemCount(): Int {
        return locationList.size
    }

    class ViewHolder(view: View, clickListener: onItemClickListener): RecyclerView.ViewHolder(view) {
        val name : TextView = itemView.findViewById(R.id.list_location_show_name)
        val city : TextView = itemView.findViewById(R.id.list_location_show_city)
        val district : TextView = itemView.findViewById(R.id.list_location_show_district)
        val description : TextView = itemView.findViewById(R.id.list_location_show_desc)
        val arrivaldate : TextView = itemView.findViewById(R.id.list_location_show_arrival_date)
        val arrivaltime : TextView = itemView.findViewById(R.id.list_location_show_arrival_time)
        val complete : TextView = itemView.findViewById(R.id.list_location_show_iscompleted)


        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }
}