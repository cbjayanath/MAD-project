package com.example.nextrip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val card01=findViewById<CardView>(R.id.c1)
        card01.setOnClickListener{
                startActivity(Intent(this,CreateTrip::class.java))
        }
    }
}