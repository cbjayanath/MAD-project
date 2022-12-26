package com.example.nextrip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.cardview.widget.CardView
import com.example.nextrip.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var card01: CardView
    private lateinit var currentTripCard: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        card01 = findViewById(R.id.c1)
        currentTripCard = findViewById(R.id.currentTripCard)

        card01.setOnClickListener{
                startActivity(Intent(this,CreateTrip::class.java))
        }

        checkIsCurrentTripAvailable()

        currentTripCard.setOnClickListener{
            back()
        }

        currentUser()
    }

    private fun checkIsCurrentTripAvailable() {

        if(intent.getStringExtra("tripid_from_location").toString().isNotEmpty()){
            card01.visibility = View.GONE
            currentTripCard.visibility = View.VISIBLE
        }else{
            card01.visibility = View.VISIBLE
            currentTripCard.visibility = View.GONE
        }
    }

    private fun currentUser() {

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("user")

        firebaseAuth = FirebaseAuth.getInstance()

        val userid = firebaseAuth.currentUser?.uid.toString()

        reference.child(userid).get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }

    private fun back(){
        val backIntent = Intent(this@MainActivity, Location::class.java)
        backIntent.putExtra("tripid", intent.getStringExtra("tripid_from_location")).toString()
        startActivity(backIntent)
    }
}