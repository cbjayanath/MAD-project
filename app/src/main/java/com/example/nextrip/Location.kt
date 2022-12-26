package com.example.nextrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.nextrip.model.ItemData
import com.example.nextrip.model.LocationData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Location : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    private lateinit var addbtn: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        addbtn = findViewById(R.id.addingButton3)

        addbtn.setOnClickListener{
            addinfo()
        }
    }

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

                val addedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                val addedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh.mm a"))

                //val addedDate =

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
}