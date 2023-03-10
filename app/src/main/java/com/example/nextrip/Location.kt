package com.example.nextrip

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nextrip.Adapters.LocationAdapter
import com.example.nextrip.model.LocationData
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Location : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    private lateinit var addbtn: FloatingActionButton
    private lateinit var locationRecyclerView: RecyclerView
    private lateinit var locationList: ArrayList<LocationData>
    private lateinit var locationAdapter: LocationAdapter

    private lateinit var backToMainMenu: Button
    private lateinit var btnfinish: Button
    private lateinit var count: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        addbtn = findViewById(R.id.addingButton3)

        backToMainMenu = findViewById(R.id.backToTheMainMenu)
        btnfinish = findViewById(R.id.finishBtn)
        count = findViewById(R.id.countLocation)

        count.visibility = View.GONE

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

        btnfinish.setOnClickListener {
            forward()
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
                    count.visibility = View.VISIBLE
                    count.text = snapshot.childrenCount.toString()
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
                }else{
                    count.visibility = View.GONE
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
        var locationArrivalDate = v.findViewById<TextInputEditText>(R.id.arrivalDate)
        var locationArrivalTime = v.findViewById<TextInputEditText>(R.id.arrivalTime)


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

            val materialTimePicker: MaterialTimePicker = MaterialTimePicker.Builder()
                .setTitleText("SELECT YOUR TIME")
                .setHour(12)
                .setMinute(10)
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .build()
            materialTimePicker.show(supportFragmentManager,"TimePicker")

            materialTimePicker.addOnPositiveButtonClickListener{
                val pickedHour = materialTimePicker.hour
                val pickedMinute = materialTimePicker.minute

                val formattedTime: String = when {
                    pickedHour > 12 -> {
                        if (pickedMinute < 10) {
                            "${materialTimePicker.hour - 12}:0${materialTimePicker.minute} pm"
                        } else {
                            "${materialTimePicker.hour - 12}:${materialTimePicker.minute} pm"
                        }
                    }
                    pickedHour == 12 -> {
                        if (pickedMinute < 10) {
                            "${materialTimePicker.hour}:0${materialTimePicker.minute} pm"
                        } else {
                            "${materialTimePicker.hour}:${materialTimePicker.minute} pm"
                        }
                    }
                    pickedHour == 0 -> {
                        if (pickedMinute < 10) {
                            "${materialTimePicker.hour + 12}:0${materialTimePicker.minute} am"
                        } else {
                            "${materialTimePicker.hour + 12}:${materialTimePicker.minute} am"
                        }
                    }
                    else -> {
                        if (pickedMinute < 10) {
                            "${materialTimePicker.hour}:0${materialTimePicker.minute} am"
                        } else {
                            "${materialTimePicker.hour}:${materialTimePicker.minute} am"
                        }
                    }
                }

                locationArrivalTime.setText(formattedTime)
            }


//            val materialTimePicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_12H).setHour(android.icu.util.Calendar.HOUR_OF_DAY).setMinute(android.icu.util.Calendar.MINUTE).setTitleText("Arrival Time").build()
//            materialTimePicker.show(supportFragmentManager, "TimePicker")
//
//            materialTimePicker.addOnPositiveButtonClickListener {
//
//                val hour = materialTimePicker.hour
//                val minute = materialTimePicker.minute
////                val time = (hour+minute).toLong()
////                locationArrivalTime.setText(time)
//            }
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

                val location = LocationData(locationid, name, city, district, description, arrivalDate, arrivalTime, addedDate, addedTime, "Not yet completed", null, null, intent.getStringExtra("tripid").toString())

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
        val backIntent = Intent(this@Location, MainMenu::class.java)
        backIntent.putExtra("tripid", intent.getStringExtra("tripid").toString())
        startActivity(backIntent)
    }

    private fun forward(){
        val backIntent = Intent(this@Location, CompleteTrip::class.java)
        backIntent.putExtra("tripid", intent.getStringExtra("tripid").toString())
        startActivity(backIntent)
    }
}