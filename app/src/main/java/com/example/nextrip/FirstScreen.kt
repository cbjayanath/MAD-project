package com.example.nextrip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class FirstScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_screen)
        var go_btn=findViewById<Button>(R.id.goBtn)

        go_btn.setOnClickListener{
            startActivity(Intent(this,Sign_In::class.java))
        }
    }
}