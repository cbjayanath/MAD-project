package com.example.nextrip

import android.content.Intent
import android.icu.text.TimeZoneFormat.TimeType
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nextrip.Adapters.BackpackAdapter
import com.example.nextrip.Adapters.LocationAdapter
import com.example.nextrip.model.ItemData
import com.example.nextrip.model.LocationData
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.database.*
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.*

class Location : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    private lateinit var addbtn: FloatingActionButton
    private lateinit var locationRecyclerView: RecyclerView
    private lateinit var locationList: ArrayList<LocationData>
    private lateinit var locationAdapter: LocationAdapter

    private lateinit var backToMainMenu: Button

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        addbtn = findViewById(R.id.addingButton3)

        backToMainMenu = findViewById(R.id.backToTheMainMenu)

        locationRecyclerView = findViewById(R.id.lRecycler)
        locationRecyclerView.layoutManager = LinearLayoutManager(this)
        locationRecyclerView.setHasFixedSize(true)

        showLocationData()

        locationList = arrayListOf<LocationData>()

        addbtn.setOnClickListener{
            addinfo()
        }

        backToMainMenu.setOnClickListener {
            back()
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

                    locationAdapter = LocationAdapter(locationList)
                    locationRecyclerView.adapter = locationAdapter
                    locationRecyclerView.visibility = View.VISIBLE

                    locationAdapter.setonItemClickListener(object : LocationAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val locationDetailsIntent = Intent(this@Location, LocationDetails::class.java)

                            locationDetailsIntent.putExtra("locationid", locationList[position].locationid)
                            locationDetailsIntent.putExtra("locationname", locationList[position].name)
                            locationDetailsIntent.putExtra("locationcity", locationList[position].city)
                            locationDetailsIntent.putExtra("locationdistrict", locationList[position].district)
                            locationDetailsIntent.putExtra("locationdescription", locationList[position].description)
                            locationDetailsIntent.putExtra("locationaddeddate", locationList[position].addedDate)
                            locationDetailsIntent.putExtra("locationaddedtime", locationList[position].addedTime)
                            locationDetailsIntent.putExtra("locationarrivaldate", locationList[position].arrivaldate)
                            locationDetailsIntent.putExtra("locationarrivaltime", locationList[position].arrivaltime)
                            locationDetailsIntent.putExtra("complete", locationList[position].complete)
                            locationDetailsIntent.putExtra("completeddate", locationList[position].completeddate)
                            locationDetailsIntent.putExtra("completedtime", locationList[position].completedtime)
                            locationDetailsIntent.putExtra("tripid", locationList[position].tripid)

                            startActivity(locationDetailsIntent)
                        }

                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addinfo() {
        val inflter = LayoutInflater.from(this)
        val v = inflter.inflate(R.layout.add_location,null)
        /**set view*/
        val locationName = v.findViewById<TextInputEditText>(R.id.lName)
        val locationCity = v.findViewById<TextInputEditText>(R.id.lCity)
        val locationDistrict = v.findViewById<TextInputEditText>(R.id.lDistrict)
        val locationDescription = v.findViewById<TextInputEditText>(R.id.lDesc)
        val locationArrivalDate = v.findViewById<TextInputEditText>(R.id.arrivalDate)
        val locationArrivalTime = v.findViewById<TextInputEditText>(R.id.arrivalTime)


        locationArrivalDate.setOnClickListener(){
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")

            datePicker.addOnPositiveButtonClickListener {
                // formatting date in dd-mm-yyyy format.
                val dateFormatter = SimpleDateFormat("dd/MMMM/yyyy")
                val date = dateFormatter.format(Date(it))
                locationArrivalDate.setText(date)
            }
        }

        locationArrivalTime.setOnClickListener {

            val materialTimePicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_12H).setHour(android.icu.util.Calendar.HOUR_OF_DAY).setMinute(android.icu.util.Calendar.MINUTE).setTitleText("Arrival Time").build()
            materialTimePicker.show(supportFragmentManager, "TimePicker")

            materialTimePicker.addOnPositiveButtonClickListener {

                val hour = materialTimePicker.hour
                val minute = materialTimePicker.minute

                val dateFormatter = SimpleDateFormat("hh.mm a")
                val time = dateFormatter.format(Time((hour + minute).toLong()))
                locationArrivalTime.setText(time)
            }
        }

        val addDialog = AlertDialog.Builder(this)
        addDialog.setView(v)
        addDialog.setPositiveButton("Add"){
                dialog,_->

            val name = locationName.text?.trim().toString()
            val city = locationCity.text?.trim().toString()
            val district = locationDistrict.text?.trim().toString()
            val description = locationDescription.text?.trim().toString()
            val arrivalDate = locationArrivalDate.text?.trim().toString()
            val arrivalTime = locationArrivalTime.text?.trim().toString()

            if(name.isEmpty()){
                Toast.makeText(this, "Name field is required!", Toast.LENGTH_LONG).show()
            }else if(isOnlyNumbers(name)){
                Toast.makeText(this, "Cannot add locations with number!", Toast.LENGTH_LONG).show()
            }else if(city.isEmpty()){
                Toast.makeText(this, "Name field is required!", Toast.LENGTH_LONG).show()
            }else if(isOnlyNumbers(city)){
                Toast.makeText(this, "Cannot add city with number!", Toast.LENGTH_LONG).show()
            }else if(district.isEmpty()){
                Toast.makeText(this, "District field is required!", Toast.LENGTH_LONG).show()
            }else if(isOnlyNumbers(district)){
                Toast.makeText(this, "Cannot add city with number!", Toast.LENGTH_LONG).show()
            }else if(description.isEmpty()){
                Toast.makeText(this, "Description field is required! Say something about $name", Toast.LENGTH_LONG).show()
            }else{

                database = FirebaseDatabase.getInstance()
                reference = database.getReference("location")

                val locationid = reference.push().key!!

                val addedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MMMM/yyyy"))
                val addedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh.mm a"))

//                val words = desc.lowercase(Locale.getDefault()).split("\\s+".toRegex())
//
//                val isrent: Boolean = "rent" in words
//                val isrented: Boolean = "rented" in words
//
//                val isfriend: Boolean = "friend" in words
//                val ismemeber: Boolean = "member" in words
//                val ismr: Boolean = "mr" in words
//
//                if(isrent || isrented){
//                    setRented("A rented item*")
//                }else if(isfriend || ismemeber || ismr){
//                    setRented("One member item")
//                }else{
//                    setRented("One of my item")
//                }

                val location = LocationData(locationid, name, city, district, description, arrivalDate, arrivalTime, addedDate, addedTime, intent.getStringExtra("tripid").toString())

                reference.child(locationid).setValue(location).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this,"$name Added Successfully!", Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this,"Cannot add $name", Toast.LENGTH_LONG).show()
                }

                dialog.dismiss()

            }
        }
        addDialog.setNegativeButton("Cancel"){
                dialog,_->
            dialog.dismiss()
        }
        addDialog.create()
        addDialog.show()
    }

    private fun isOnlyNumbers(q: String): Boolean {
        val regex = "^[0-9]*$".toRegex()
        return regex.matches(q)
    }

    private fun back(){
        val backIntent = Intent(this@Location, MainActivity::class.java)
        backIntent.putExtra("tripid_from_location", intent.getStringExtra("tripid")).toString()
        startActivity(backIntent)
    }
}