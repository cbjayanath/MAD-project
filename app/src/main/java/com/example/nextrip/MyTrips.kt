package com.example.nextrip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nextrip.Adapters.BackpackAdapter
import com.example.nextrip.Adapters.TripsAdapter
import com.example.nextrip.model.ItemData
import com.example.nextrip.model.TripData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.ArrayList

class MyTrips : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var tripRecyclerView: RecyclerView
    private lateinit var tripList: ArrayList<TripData>
    private lateinit var tripsAdapter: TripsAdapter

    private lateinit var count: TextView

    private lateinit var btnback: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_trips)

        btnback = findViewById(R.id.my_trip_btn_back)
        count = findViewById(R.id.countTripComplete)

        tripRecyclerView = findViewById(R.id.tRecycler)
        tripRecyclerView.layoutManager = LinearLayoutManager(this)
        tripRecyclerView.setHasFixedSize(true)

//        loadicon = findViewById(R.id.image_loadicon)
//        Glide.with(this).load(R.drawable.loadicon).into(loadicon)

        tripList = arrayListOf<TripData>()

        showTripsData()

        showCountofRecycleView()

        btnback.setOnClickListener {
            back()
        }
    }

    private fun showCountofRecycleView() {
        if(tripRecyclerView.adapter?.getItemCount().toString().isEmpty()){
            tripRecyclerView.visibility = View.GONE
            count.visibility = View.GONE
        }else{
            tripRecyclerView.visibility = View.VISIBLE
            count.visibility = View.VISIBLE
            count.text = tripRecyclerView.adapter?.getItemCount().toString()
        }
    }

    private fun showTripsData() {
        tripRecyclerView.visibility = View.GONE
        //loadicon.visibility = View.VISIBLE

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("trip")

        firebaseAuth = FirebaseAuth.getInstance()
        val end = firebaseAuth.currentUser?.uid.toString() + "end"

        reference.orderByChild("end").equalTo(end).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tripList.clear()
                if(snapshot.exists()){
                    for(t in snapshot.children){
                        val tripData = t.getValue(TripData::class.java)
                        if (tripData != null) {
                            tripList.add(tripData)
                        }
                    }

                    tripsAdapter = TripsAdapter(tripList)
                    tripRecyclerView.adapter = tripsAdapter
                    tripRecyclerView.visibility = View.VISIBLE

                    tripsAdapter.setonItemClickListener(object : TripsAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val tripDetailsIntent = Intent(this@MyTrips, CompleteTripInformation::class.java)

                            tripDetailsIntent.putExtra("tripid", tripList[position].tripid)
                            tripDetailsIntent.putExtra("tripname", tripList[position].tripname)
                            tripDetailsIntent.putExtra("tripdescription", tripList[position].tripdescription)
                            tripDetailsIntent.putExtra("tripstartdate", tripList[position].startdate)
                            tripDetailsIntent.putExtra("tripenddate", tripList[position].enddate)
                            tripDetailsIntent.putExtra("tripendtime", tripList[position].endtime)
                            tripDetailsIntent.putExtra("tripend", tripList[position].end)

                            startActivity(tripDetailsIntent)
                        }

                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun back() {
        val backpackIntent = Intent(this@MyTrips,MainActivity::class.java)
        startActivity(backpackIntent)
    }
}