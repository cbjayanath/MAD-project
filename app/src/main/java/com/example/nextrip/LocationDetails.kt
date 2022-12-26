package com.example.nextrip

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LocationDetails : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    private lateinit var btnback: ImageView

    private lateinit var name: TextView
    private lateinit var city: TextView
    private lateinit var district: TextView
    private lateinit var desc: TextView
    private lateinit var date: TextView
    private lateinit var time: TextView

    private lateinit var btnmap: FloatingActionButton
    private lateinit var btnnavigation: FloatingActionButton
    private lateinit var btnshare: FloatingActionButton
    private lateinit var btnedit: FloatingActionButton
    private lateinit var btndelete: FloatingActionButton
    private lateinit var btncomplete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_details)

        btnback = findViewById(R.id.location_details_btn_back)

        name = findViewById(R.id.location_details_txt_show_name)
        city = findViewById(R.id.location_details_txt_show_city)
        district = findViewById(R.id.location_details_txt_show_district)
        desc = findViewById(R.id.location_details_txt_show_description)
        date = findViewById(R.id.location_details_txt_show_date)
        time = findViewById(R.id.location_details_txt_show_time)

        btnmap = findViewById(R.id.details_location_btn_map)
        btnnavigation = findViewById(R.id.details_location_btn_navigation)
        btnshare = findViewById(R.id.details_location_btn_share)
        btnedit = findViewById(R.id.details_location_btn_update)
        btndelete = findViewById(R.id.details_location_btn_delete)
        btncomplete = findViewById(R.id.details_location_btn_complete)

        showLocationDetails()

        btnback.setOnClickListener {
            back()
        }

        btnmap.setOnClickListener{
            showInMap(intent.getStringExtra("locationname").toString())
        }

        btnnavigation.setOnClickListener{
            showInMapNavigation(intent.getStringExtra("locationname").toString())
        }

        btnshare.setOnClickListener{
            shareLocation(intent.getStringExtra("locationname").toString(), intent.getStringExtra("locationcity").toString(), intent.getStringExtra("locationdistrict").toString(), intent.getStringExtra("locationdescription").toString())
        }

        btndelete.setOnClickListener{
            deleteRecord(intent.getStringExtra("locationid").toString(), intent.getStringExtra("locationname").toString())
        }
    }

    private fun showLocationDetails() {
        name.text = intent.getStringExtra("locationname").toString()
        city.text = intent.getStringExtra("locationcity").toString() + "city"
        district.text = "in " + intent.getStringExtra("locationdistrict").toString() + " district"
        desc.text = intent.getStringExtra("locationdescription").toString()
        date.text = intent.getStringExtra("locationaddeddate").toString() + " - " + intent.getStringExtra("locationarrivaldate").toString()
        time.text = intent.getStringExtra("locationaddedtime").toString() + " - " + intent.getStringExtra("locationarrivaltime").toString()

    }

    private fun showInMap(name: String ?= null){
        val gmmIntentUri = Uri.parse("geo:0,0?q=$name")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    private fun showInMapNavigation(name: String ?= null){
        val gmmIntentUri = Uri.parse("google.navigation:q=$name")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    private fun shareLocation(name: String ?= null, city: String ?= null, district: String ?= null, desc: String ?= null) {

        val itemDetail = "$name located near to the $city $district. So, $desc"

        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, itemDetail)
        intent.type = "text/plain"

        if(itemDetail.isNotEmpty()){
            startActivity(Intent.createChooser(intent, "Share To : "))
            if(isFinishing){
                Toast.makeText(this, "$name shared successfully!", Toast.LENGTH_LONG).show()
            }
        }else if (itemDetail.isEmpty()){
            Toast.makeText(this, "Null content cannot share!", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this, "Cannot share $name!", Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteRecord(id: String, name: String) {
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("location").child(id)

        reference.removeValue().addOnSuccessListener {
            Toast.makeText(this, "$name deleted successfully!", Toast.LENGTH_LONG).show()
            back()
        }.addOnFailureListener { error ->
            Toast.makeText(this, "Cannot delete $name. Error : ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun back(){
        val backIntent = Intent(this@LocationDetails, Location::class.java)
        backIntent.putExtra("tripid", intent.getStringExtra("tripid")).toString()
        startActivity(backIntent)
    }
}