package com.example.nextrip.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nextrip.R
import com.example.nextrip.model.LocationData

class LocationCompleteAdapter(private val locationList: ArrayList<LocationData>): RecyclerView.Adapter<LocationCompleteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_completed_location, parent, false)
        return LocationCompleteAdapter.ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentLocation = locationList[position]
        holder.name.text = currentLocation.name.toString()
        holder.city.text = currentLocation.city.toString()
        holder.district.text =  currentLocation.district.toString()
        holder.description.text = currentLocation.description.toString()
        holder.addeddatetime.text = "Added on " + currentLocation.addedDate.toString() + " at " + currentLocation.arrivaltime.toString()
        holder.completeddate.text = "Completed on " + currentLocation.completeddate.toString() + " at " + currentLocation.completedtime.toString()
        holder.complete.text = currentLocation.complete.toString()
    }

    override fun getItemCount(): Int {
        return locationList.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name : TextView = itemView.findViewById(R.id.list_location_txt_show_name)
        val city : TextView = itemView.findViewById(R.id.list_location_txt_show_city)
        val district : TextView = itemView.findViewById(R.id.list_location_txt_show_district)
        val description : TextView = itemView.findViewById(R.id.list_location_txt_show_description)
        val addeddatetime : TextView = itemView.findViewById(R.id.list_location_txt_show_startdatetime)
        val completeddate : TextView = itemView.findViewById(R.id.list_location_txt_show_completedatetime)
        val complete : TextView = itemView.findViewById(R.id.list_location_txt_show_iscompleted)
    }
}