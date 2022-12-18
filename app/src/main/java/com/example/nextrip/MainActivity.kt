package com.example.nextrip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val card01=findViewById<CardView>(R.id.c1)

        card01.setOnClickListener{
                startActivity(Intent(this,CreateTrip::class.java))
        }

        currentUser()
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
}