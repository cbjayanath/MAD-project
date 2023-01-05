package com.example.nextrip

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.nextrip.databinding.ActivityMapsEmUsersBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity_em_users : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsEmUsersBinding

    private lateinit var backbtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsEmUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        backbtn = findViewById(R.id.backbtnemusers)

        backbtn.setOnClickListener {
            back()
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(intent.getStringExtra("lati").toString().toDouble(), intent.getStringExtra("long").toString().toDouble())
        mMap.addMarker(MarkerOptions().position(sydney).title(intent.getStringExtra("userid").toString()))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 7f))
    }

    private fun back() {
        val backpackIntent = Intent(this@MapsActivity_em_users,Emergancy::class.java)
        backpackIntent.putExtra("tripid", intent.getStringExtra("tripid").toString())
        backpackIntent.putExtra("userid", intent.getStringExtra("userid").toString())
        backpackIntent.putExtra("lati", intent.getStringExtra("lati").toString())
        backpackIntent.putExtra("long", intent.getStringExtra("long").toString())
        backpackIntent.putExtra("location", intent.getStringExtra("location").toString())
        startActivity(backpackIntent)
    }
}