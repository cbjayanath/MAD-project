package com.example.nextrip

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class Sign_In : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        var signin_btn=findViewById<Button>(R.id.signInBtn)
        var signup_txt=findViewById<TextView>(R.id.signUpHereText)

        signin_btn.setOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
        }

        signup_txt.setOnClickListener{
            startActivity(Intent(this,Sign_up::class.java))
        }
    }
}