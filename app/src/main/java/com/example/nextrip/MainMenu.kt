package com.example.nextrip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView

class MainMenu : AppCompatActivity() {

    private lateinit var currenttripcard: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        currenttripcard = findViewById(R.id.currentTripCard)

        currenttripcard.setOnClickListener{
            back()
        }

    }

    private fun back(){
        val backIntent = Intent(this@MainMenu, Location::class.java)
        backIntent.putExtra("tripid", intent.getStringExtra("tripid").toString())
        startActivity(backIntent)
    }
}