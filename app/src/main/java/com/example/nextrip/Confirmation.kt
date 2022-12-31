package com.example.nextrip

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class Confirmation : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var btnback: ImageView
    private lateinit var btnsendtext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        btnback = findViewById(R.id.confirm_btn_back)
        btnsendtext = findViewById(R.id.confirm_sendMailBtn)

        btnback.setOnClickListener {
            back()
        }

        btnsendtext.setOnClickListener {
            sendConfrimationCode()
        }
    }

    private fun sendConfrimationCode(){

        firebaseAuth = FirebaseAuth.getInstance()

        val confirmationCode = (10000..99999).random().toString()

        val backIntent = Intent(this@Confirmation, Finish::class.java)
        backIntent.putExtra("tripid", intent.getStringExtra("tripid")).toString()
        backIntent.putExtra("code", confirmationCode).toString()
        startActivity(backIntent)
    }

    private fun back(){
        val backIntent = Intent(this@Confirmation, Location::class.java)
        backIntent.putExtra("tripid", intent.getStringExtra("tripid")).toString()
        startActivity(backIntent)
    }
}