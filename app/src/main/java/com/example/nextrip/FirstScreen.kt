package com.example.nextrip

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class FirstScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_screen)
//        var go_btn=findViewById<Button>(R.id.goBtn)
//
//        go_btn.setOnClickListener{
//            startActivity(Intent(this,Sign_In::class.java))
        supportActionBar?.hide()
        Handler().postDelayed({
            val intent =Intent(this@FirstScreen,Sign_In::class.java)
            startActivity(intent)
            finish()
        },3000)

    }
}