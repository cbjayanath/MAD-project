package com.example.nextrip

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import com.example.nextrip.model.TripData
import com.google.android.material.textfield.TextInputEditText
import java.util.*
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat


class CreateTrip : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var tripname: TextInputEditText
    private lateinit var tripdescription: TextInputEditText
    private lateinit var d_text:TextInputEditText
    private lateinit var btnCareateTrip: Button


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.nextrip.R.layout.activity_create_trip)

        tripname = findViewById(R.id.txt_input_createtrip_name)
        tripdescription = findViewById(R.id.txt_input_createtrip_desc)
        d_text=findViewById(R.id.txt_date)
        btnCareateTrip=findViewById(R.id.createTripBtn)


        d_text.setOnClickListener(){
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")

            datePicker.addOnPositiveButtonClickListener {
                // formatting date in dd-mm-yyyy format.
                val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
                val date = dateFormatter.format(Date(it))
                d_text.setText(date)

            }
        }

        btnCareateTrip.setOnClickListener{

            createTrip()
        }
    }

    private fun createTrip() {

        firebaseAuth = FirebaseAuth.getInstance()

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("trip")

        val tripname = tripname.text.toString()
        val tripdesc = tripdescription.text.toString()

        val userid = firebaseAuth.currentUser?.uid.toString()

        val tripid = reference.push().key!!

        val trip = TripData(tripid, tripname, tripdesc, d_text.text.toString(), d_text.text.toString(), userid, false)

        reference.child(tripid).setValue(trip).addOnCompleteListener{
            if(it.isSuccessful){
                val tripIntent = Intent(this@CreateTrip, Member::class.java)
                tripIntent.putExtra("tripid", tripid)
                startActivity(tripIntent)
                Toast.makeText(this, "$tripname created successfully!", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Cannot create $tripname!", Toast.LENGTH_LONG).show()
        }
    }


}


