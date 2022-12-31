package com.example.nextrip.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nextrip.R
import com.example.nextrip.model.TripData
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.NonDisposableHandle
import kotlinx.coroutines.NonDisposableHandle.parent

class TripsAdapter (private val tripList: ArrayList<TripData>): RecyclerView.Adapter<TripsAdapter.ViewHolder>() {

    private lateinit var tripListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setonItemClickListener(clickListener: onItemClickListener){
        tripListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_trip, parent, false)
        return TripsAdapter.ViewHolder(itemView, tripListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTrip = tripList[position]
        holder.name.text = currentTrip.tripname.toString()
        holder.description.text = currentTrip.tripdescription.toString()
    }

    override fun getItemCount(): Int {
        return tripList.size
    }

    class ViewHolder(view: View, clickListener: TripsAdapter.onItemClickListener): RecyclerView.ViewHolder(view)  {

        val name : TextView = itemView.findViewById(R.id.list_trip_show_name)
        val description : TextView = itemView.findViewById(R.id.list_trip_show_desc)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }
}