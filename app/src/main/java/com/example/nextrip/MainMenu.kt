package com.example.nextrip

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import android.net.Uri
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.text.format.Formatter.formatIpAddress
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.util.*
import java.util.jar.Manifest

class MainMenu : AppCompatActivity() {

    private lateinit var currenttripcard: CardView
    private lateinit var nearbyhotelscard: CardView

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: com.google.android.gms.location.LocationRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        currenttripcard = findViewById(R.id.currentTripCard)
        nearbyhotelscard = findViewById(R.id.nearbyHotelsTripCard)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        currenttripcard.setOnClickListener{
            forword()
        }

        nearbyhotelscard.setOnClickListener {
            openNearByHotelsMap()
        }

        val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        val ipaddress = formatIpAddress(wifiManager.connectionInfo.ipAddress)

    }

    private fun forword(){
        val backIntent = Intent(this@MainMenu, Member::class.java)
        backIntent.putExtra("tripid", intent.getStringExtra("tripid")).toString()
        startActivity(backIntent)
    }

    private fun openNearByHotelsMap(){
//        val gmmIntentUri = Uri.parse("geo:37.7749,-122.4194?q=restaurants")
//        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//        mapIntent.setPackage("com.google.android.apps.maps")
//        startActivity(mapIntent)

        val backIntent = Intent(this@MainMenu, Member::class.java)
        //backIntent.putExtra("tripid", intent.getStringExtra("tripid")).toString()
        startActivity(backIntent)
    }
}