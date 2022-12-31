package com.example.nextrip

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView

class MainMenu : AppCompatActivity() {

    private lateinit var currenttripcard: CardView
    private lateinit var nearbyhotelscard: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        currenttripcard = findViewById(R.id.currentTripCard)
        nearbyhotelscard = findViewById(R.id.nearbyHotelsTripCard)

        currenttripcard.setOnClickListener{
            forword()
        }

        nearbyhotelscard.setOnClickListener {
            openNearByHotelsMap()
        }

    }

    private fun forword(){
        val backIntent = Intent(this@MainMenu, Member::class.java)
        backIntent.putExtra("tripid", intent.getStringExtra("tripid")).toString()
        startActivity(backIntent)
    }

    private fun openNearByHotelsMap(){
        val gmmIntentUri = Uri.parse("geo:0,0?q=hotels")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }
}