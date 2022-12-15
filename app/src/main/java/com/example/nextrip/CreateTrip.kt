package com.example.nextrip

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import java.util.*
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat


class CreateTrip : AppCompatActivity() {
    private lateinit var d_text:TextInputEditText


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.nextrip.R.layout.activity_create_trip)

        d_text=findViewById(com.example.nextrip.R.id.txt_date)
        var ct_btn=findViewById<Button>(R.id.createTripBtn)


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

        ct_btn.setOnClickListener{
            startActivity(Intent(this,Member::class.java))
        }


    }

    }


