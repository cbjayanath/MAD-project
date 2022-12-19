package com.example.nextrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MemberDetail : AppCompatActivity() {

    private lateinit var txtmemberName: TextView
    private lateinit var txtmemberPhonenumber: TextView
    private lateinit var txtEmcontact: TextView
    private lateinit var txtmemberAddress: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_detail)

        txtmemberName = findViewById(R.id.txt_show_member_name)
        txtmemberPhonenumber = findViewById(R.id.txt_show_member_phonenumber)
        txtEmcontact = findViewById(R.id.txt_show_member_emergancy_contact)
        txtmemberAddress = findViewById(R.id.txt_show_member_address)

        showMemberCredentional()
    }

    private fun showMemberCredentional() {

        txtmemberName.text = intent.getStringExtra("membername")
        txtmemberPhonenumber.text = intent.getStringExtra("memberphonenumber")
        txtEmcontact.text = intent.getStringExtra("memberemcontact")
        txtmemberAddress.text = intent.getStringExtra("memberaddress")
    }
}