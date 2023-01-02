package com.example.nextrip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nextrip.Adapters.LocationAdapter
import com.example.nextrip.Adapters.LocationCompleteAdapter
import com.example.nextrip.model.LocationData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import java.util.ArrayList

class LocationCompleted : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    private lateinit var locationRecyclerView: RecyclerView
    private lateinit var locationList: ArrayList<LocationData>
    private lateinit var locationAdapter: LocationCompleteAdapter

    private lateinit var count: TextView

    private lateinit var btnback: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_completed)

        btnback = findViewById(R.id.cl_trip_btn_back)
        count = findViewById(R.id.countLocationComplete)

        locationRecyclerView = findViewById(R.id.cLRecycler)
        locationRecyclerView.layoutManager = LinearLayoutManager(this)
        locationRecyclerView.setHasFixedSize(true)

        showLocationData()

        showCountofRecycleView()

        locationList = arrayListOf<LocationData>()

        btnback.setOnClickListener {
            back()
        }
    }

    private fun showCountofRecycleView() {
        if(locationRecyclerView.adapter?.getItemCount().toString().isEmpty()){
            locationRecyclerView.visibility = View.GONE
            count.visibility = View.GONE
        }else{
            locationRecyclerView.visibility = View.VISIBLE
            count.visibility = View.VISIBLE
            count.text = locationRecyclerView.adapter?.getItemCount().toString()
        }
    }

    private fun showLocationData() {
        locationRecyclerView.visibility = View.GONE
        //loadicon.visibility = View.VISIBLE

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("location")

        reference.orderByChild("tripid").equalTo(intent.getStringExtra("tripid").toString()).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                locationList.clear()
                if(snapshot.exists()){
                    for(li in snapshot.children){
                        val locationData = li.getValue(LocationData::class.java)
                        if (locationData != null) {
                            locationList.add(locationData)
                        }
                    }

                    locationAdapter = LocationCompleteAdapter(locationList)
                    locationRecyclerView.adapter = locationAdapter
                    locationRecyclerView.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun back() {
        val backpackIntent = Intent(this@LocationCompleted,CompleteTripInformation::class.java)
        backpackIntent.putExtra("tripid", intent.getStringExtra("tripid").toString())
        backpackIntent.putExtra("tripname", intent.getStringExtra("tripname").toString())
        backpackIntent.putExtra("tripdescription", intent.getStringExtra("tripdescription").toString())
        backpackIntent.putExtra("tripstartdate", intent.getStringExtra("tripstartdate").toString())
        backpackIntent.putExtra("tripenddate", intent.getStringExtra("tripenddate").toString())
        backpackIntent.putExtra("tripendtime", intent.getStringExtra("tripendtime").toString())
        startActivity(backpackIntent)
    }
}