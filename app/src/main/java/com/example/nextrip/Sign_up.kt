package com.example.nextrip

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Sign_up : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        var signup_btn=findViewById<Button>(R.id.signUptBtn)

        signup_btn.setOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}