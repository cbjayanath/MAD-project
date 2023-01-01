package com.example.nextrip

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.nextrip.model.LocationData
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

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
    private lateinit var complete: TextView
    private lateinit var locationQR: ImageView
    private lateinit var hinticon: ImageView
    private lateinit var hint: TextView
    private lateinit var ignore: TextView

    private lateinit var btnmap: FloatingActionButton
    private lateinit var btnnavigation: FloatingActionButton
    private lateinit var btnshare: FloatingActionButton
    private lateinit var btnedit: FloatingActionButton
    private lateinit var btndelete: FloatingActionButton
    private lateinit var btncomplete: Button

    @RequiresApi(Build.VERSION_CODES.O)
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
        complete = findViewById(R.id.location_details_txt_show_completed)
        locationQR = findViewById(R.id.location_details_show_img_qrcode)
        hinticon = findViewById(R.id.location_details_show_img_hinticon)
        hint = findViewById(R.id.location_details_txt_show_hint)
        ignore = findViewById(R.id.location_details_txt_show_ignore)

        btnmap = findViewById(R.id.details_location_btn_map)
        btnnavigation = findViewById(R.id.details_location_btn_navigation)
        btnshare = findViewById(R.id.details_location_btn_share)
        btnedit = findViewById(R.id.details_location_btn_update)
        btndelete = findViewById(R.id.details_location_btn_delete)
        btncomplete = findViewById(R.id.details_location_btn_complete)

        showLocationDetails()

        showLocationQR()

        checkIsComplete()

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

        btnedit.setOnClickListener{
            updateLocationInfo()
        }

        btndelete.setOnClickListener{
            deleteRecord(intent.getStringExtra("locationid").toString(), intent.getStringExtra("locationname").toString())
        }

        btncomplete.setOnClickListener {
            setAsComplete(intent.getStringExtra("locationid").toString(), intent.getStringExtra("locationname").toString())
        }
    }

    private fun showLocationDetails() {
        name.text = intent.getStringExtra("locationname").toString()
        city.text = intent.getStringExtra("locationcity").toString() + " City"
        district.text =  intent.getStringExtra("locationdistrict").toString() + " District"
        desc.text = intent.getStringExtra("locationdescription").toString()
        date.text = intent.getStringExtra("locationaddeddate").toString() + " to " + intent.getStringExtra("locationarrivaldate").toString()
        time.text = intent.getStringExtra("locationaddedtime").toString() + " to " + intent.getStringExtra("locationarrivaltime").toString()

        val words = intent.getStringExtra("locationdescription").toString().lowercase(Locale.getDefault()).split("\\s+".toRegex())

        val ishistory: Boolean = "history" in words
        val ishistorical: Boolean = "historical" in words
        val isking: Boolean = "king" in words

        val isnature: Boolean = "nature" in words
        val isnatural: Boolean = "natural" in words
        val ismountain: Boolean = "mountain" in words
        val iswaterfall: Boolean = "waterfall" in words
        val isforest: Boolean = "forest" in words

        if(ishistory || ishistorical || isking){

            hinticon.visibility = View.VISIBLE
            hint.visibility = View.VISIBLE
            ignore.visibility = View.VISIBLE

            hint.text = "Seems like you will see one of the historical places. Find out more about the history of this place before having fun. It will help you to update your knowledge about history."

            ignore.setOnClickListener {
                hideHint()
            }

        }else if(isnatural || isnature){

            hinticon.visibility = View.VISIBLE
            hint.visibility = View.VISIBLE
            ignore.visibility = View.VISIBLE

            hint.text = "Seems like you will see the beauty of nature. Have fun and save nature for other travelers. Have a nice trip!"

            ignore.setOnClickListener {
                hideHint()
            }

        }else if(ismountain){

            hinticon.visibility = View.VISIBLE
            hint.visibility = View.VISIBLE
            ignore.visibility = View.VISIBLE

            hint.text = "Seems like you will see a mountain. If you have permission and more time, we suggest climbing the mountain. It will be more funny and memorable."

            ignore.setOnClickListener {
                hideHint()
            }

        }else if(iswaterfall){

            hinticon.visibility = View.VISIBLE
            hint.visibility = View.VISIBLE
            ignore.visibility = View.VISIBLE

            hint.text = "Seems like you will see a beautiful waterfall. If you have permission and more time we suggest swimming, bathing, and other fun activities. Before having fun make sure that the waterfall is safe."

            ignore.setOnClickListener {
                hideHint()
            }

        }else if(isforest){

            hinticon.visibility = View.VISIBLE
            hint.visibility = View.VISIBLE
            ignore.visibility = View.VISIBLE

            hint.text = "Seems like you will see a beautiful forest. Take a lot of time and see the beauty of nature with your eyes. It will be nicer than taking photos from electronic devices quickly."

            ignore.setOnClickListener {
                hideHint()
            }

        }else{
            hideHint()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateLocationInfo() {
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
//                val time = (hour+minute).toLong()
//                locationArrivalTime.setText(time)
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

                val locationid = intent.getStringExtra("locationid").toString()

                val addedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MMMM/yyyy"))
                val addedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh.mm a"))

                val location = LocationData(locationid, name, city, district, description, arrivalDate, arrivalTime, addedDate, addedTime, "Not yet completed", null, null, intent.getStringExtra("tripid").toString())

                reference.child(locationid).setValue(location).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this,"$name Updated Successfully!", Toast.LENGTH_LONG).show()
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

    private fun showLocationQR() {

        val qr = intent.getStringExtra("locationname").toString() + " in " + intent.getStringExtra("locationcity").toString()

        if(qr.isEmpty()){
            locationQR.visibility = View.GONE
        }else if(qr.isNotEmpty()){

            val writer = QRCodeWriter()

            try {
                val bitMatrix = writer.encode(qr, BarcodeFormat.QR_CODE, 100, 100)
                val width = bitMatrix.width
                val height = bitMatrix.height
                val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

                for(x in 0 until width){
                    for(y in 0 until height){
                        bmp.setPixel(x,y, if(bitMatrix[x,y]) Color.BLUE else Color.WHITE)
                    }
                }

                locationQR.setImageBitmap(bmp)

            }catch (ex: WriterException){
                locationQR.visibility = View.INVISIBLE
                Toast.makeText(this, "QR automatically hide because of $ex", Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show()
        }
    }

    private fun showInMap(name: String ?= null){
        val gmmIntentUri = Uri.parse("geo:0,0?q=$name&q=restaurants")
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setAsComplete(id: String, name: String) {

        val completeddate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MMMM/yyyy"))
        val completedtime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh.mm a"))

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("location").child(id)

        reference.child("complete").setValue("Completed").addOnCompleteListener{
            if(it.isSuccessful){

                reference.child("completeddate").setValue(completeddate).addOnCompleteListener{
                    if(it.isSuccessful){
                    }
                }

                reference.child("completedtime").setValue(completedtime).addOnCompleteListener{
                    if(it.isSuccessful){
                    }
                }
                Toast.makeText(this,"$name Completed Successfully!", Toast.LENGTH_LONG).show()
                back()
            }
        }.addOnFailureListener {
            Toast.makeText(this,"Cannot add $name", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkIsComplete(){

        val completed = intent.getStringExtra("complete").toString()
        val date = intent.getStringExtra("completeddate").toString()
        val time = intent.getStringExtra("completedtime").toString()

        if(completed=="Completed"){
            btncomplete.visibility = View.GONE
            btnedit.visibility = View.GONE
            btndelete.visibility = View.GONE
            complete.visibility = View.VISIBLE
            btnnavigation.visibility=View.GONE
            btnshare.x=100f
            btnmap.x=210f
            complete.text = "Completed on $date at $time"

        }else{
            btncomplete.visibility = View.VISIBLE
            btnedit.visibility = View.VISIBLE
            btndelete.visibility = View.VISIBLE
            complete.visibility = View.GONE
        }
    }

    private fun hideHint(){
        hinticon.visibility = View.GONE
        hint.visibility = View.GONE
        ignore.visibility = View.GONE
    }

    private fun isOnlyNumbers(q: String): Boolean {
        val regex = "^[0-9]*$".toRegex()
        return regex.matches(q)
    }

    private fun back(){
        val backIntent = Intent(this@LocationDetails, Location::class.java)
        backIntent.putExtra("tripid", intent.getStringExtra("tripid")).toString()
        startActivity(backIntent)
    }
}