package com.example.nextrip

import android.content.Intent
import android.media.audiofx.DynamicsProcessing.Config
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.text.input.KeyboardType.Companion.Email
import com.google.firebase.auth.FirebaseAuth
import papaya.`in`.sendmail.SendMail


class Confirmation : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var btnback: Button
    private lateinit var btnsendtext: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        btnback = findViewById(R.id.confirm_btn_back)
        btnsendtext = findViewById(R.id.confirm_sendMailBtn)

        btnback.setOnClickListener {
            back()
        }

        btnsendtext.setOnClickListener {
            sendConfrimationCode()
            forward()
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)
    }

    private fun sendConfrimationCode(){

        firebaseAuth = FirebaseAuth.getInstance()

        val confirmationCode = (10000..99999).random().toString()
    }

    private fun forward(){
        val backIntent = Intent(this@Confirmation, CompleteTrip::class.java)
        backIntent.putExtra("tripid", intent.getStringExtra("tripid")).toString()
        startActivity(backIntent)
    }

    private fun back(){
        val backIntent = Intent(this@Confirmation, Location::class.java)
        backIntent.putExtra("tripid", intent.getStringExtra("tripid")).toString()
        startActivity(backIntent)
    }
}