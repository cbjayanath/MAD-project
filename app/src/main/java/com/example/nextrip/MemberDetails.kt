package com.example.nextrip

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.nextrip.model.MemberData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import java.util.regex.Pattern

class MemberDetails : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    private lateinit var btnback: ImageView

    private lateinit var name: TextView
    private lateinit var phoneNumber: TextView
    private lateinit var emergencyNumber: TextView
    private lateinit var address: TextView
    private lateinit var emergencyNumberQR: ImageView

    private lateinit var btnshare: FloatingActionButton
    private lateinit var btnedit: FloatingActionButton
    private lateinit var btndelete: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_details)

        btnback = findViewById(R.id.member_details_btn_back)
        btnshare = findViewById(R.id.details_member_btn_share)
        btnedit = findViewById(R.id.details_member_btn_update)
        btndelete = findViewById(R.id.details_member_btn_delete)

        name = findViewById(R.id.member_details_txt_show_name)
        phoneNumber = findViewById(R.id.member_details_txt_show_phonenumber)
        emergencyNumber = findViewById(R.id.member_details_txt_show_em_contact)
        address = findViewById(R.id.member_details_txt_show_address)
        emergencyNumberQR = findViewById(R.id.member_details_show_img_qrcode)

        btnback.setOnClickListener {

            val backIntent = Intent(this@MemberDetails, Member::class.java)
            backIntent.putExtra("tripid", intent.getStringExtra("tripid")).toString()
            startActivity(backIntent)
        }

        showMemberDetails()

        showMemberEmContactQR()

        btnshare.setOnClickListener{
            shareMember(intent.getStringExtra("membername").toString(), intent.getStringExtra("memberphonenumber").toString(), intent.getStringExtra("memberemcontact").toString(), intent.getStringExtra("memberaddress").toString())
        }

        btnedit.setOnClickListener{
            updateMemberDetails()
        }

        btndelete.setOnClickListener {
            deleteRecord(intent.getStringExtra("memberphonenumber").toString(), intent.getStringExtra("membername").toString())
        }
    }

    private fun showMemberDetails() {
        name.text = intent.getStringExtra("membername").toString()
        phoneNumber.text = intent.getStringExtra("memberphonenumber").toString()
        emergencyNumber.text = intent.getStringExtra("memberemcontact").toString()
        address.text = intent.getStringExtra("memberaddress").toString()
    }

    private fun showMemberEmContactQR() {

        val qr = intent.getStringExtra("memberemcontact").toString()

        if(qr.isEmpty()){
            emergencyNumberQR.visibility = View.GONE
        }else if(qr.isNotEmpty()){

            val writer = QRCodeWriter()

            try {
                val bitMatrix = writer.encode(qr, BarcodeFormat.QR_CODE, 150, 150)
                val width = bitMatrix.width
                val height = bitMatrix.height
                val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

                for(x in 0 until width){
                    for(y in 0 until height){
                        bmp.setPixel(x,y, if(bitMatrix[x,y]) Color.RED else Color.WHITE)
                    }
                }

                emergencyNumberQR.setImageBitmap(bmp)

            }catch (ex: WriterException){
                emergencyNumberQR.visibility = View.INVISIBLE
                Toast.makeText(this, "QR automatically hide because of $ex", Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show()
        }
    }


    private fun shareMember(name: String ?= null, phone: String ?= null, emergencyNumber: String ?= null, address: String ?= null) {

        val memberDetail = "$name $phone Emergency contact is $emergencyNumber and Address is $address"

        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, memberDetail)
        intent.type = "text/plain"

        if(memberDetail.isNotEmpty()){
            startActivity(Intent.createChooser(intent, "Share To : "))
            if(isFinishing){
                Toast.makeText(this, "$name shared successfully!", Toast.LENGTH_LONG).show()
            }
        }else if (memberDetail.isEmpty()){
            Toast.makeText(this, "Null content cannot share!", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this, "Cannot share $name!", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateMemberDetails() {

        val inflter = LayoutInflater.from(this)
        val v = inflter.inflate(R.layout.add_member,null)
        /**set view*/
        val memberName = v.findViewById<TextInputEditText>(R.id.mName)
        val memberTel = v.findViewById<TextInputEditText>(R.id.mTelephone)
        val memberEmergency=v.findViewById<TextInputEditText>(R.id.mEmergency)
        val memberAddress=v.findViewById<TextInputEditText>(R.id.mAddress)

        memberName.setText(intent.getStringExtra("membername").toString())
        memberTel.setText(intent.getStringExtra("memberphonenumber").toString())
        memberEmergency.setText(intent.getStringExtra("memberemcontact").toString())
        memberAddress.setText(intent.getStringExtra("memberaddress").toString())

        val addDialog = AlertDialog.Builder(this)
        addDialog.setView(v)
        addDialog.setPositiveButton("Add"){
                dialog,_->

            val name = memberName.text?.trim().toString()
            val number = memberTel.text?.trim().toString()
            val emergency=memberEmergency.text?.trim().toString()
            val address=memberAddress.text?.trim().toString()

            if(name.isEmpty()){
                Toast.makeText(this, "Name field is required!", Toast.LENGTH_LONG).show()
            }else if(!isOnlyLetters(name)){
                Toast.makeText(this, "Invalid name! Please give first name", Toast.LENGTH_LONG).show()
            }else if(number.isEmpty()){
                Toast.makeText(this, "Phone number is required!", Toast.LENGTH_LONG).show()
            }else if(!isValidPhoneNumber(number) && number.length == 10){
                Toast.makeText(this, "Phone number is not valid!", Toast.LENGTH_LONG).show()
            }else if(emergency.isEmpty()){
                Toast.makeText(this, "Emergency contact is required!", Toast.LENGTH_LONG).show()
            }else if(!isValidPhoneNumber(emergency)){
                Toast.makeText(this, "Phone number is not valid!", Toast.LENGTH_LONG).show()
            }else if(address.isEmpty()){
                Toast.makeText(this, "Address is required!", Toast.LENGTH_LONG).show()
            }else{

                database = FirebaseDatabase.getInstance()
                reference = database.getReference("member")

                val member = MemberData(name, number, emergency, address, intent.getStringExtra("tripid"))

                reference.child(number).child(member.toString()).setValue(member).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this,"$name Added Successfully!",Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this,"Cannot add $name",Toast.LENGTH_LONG).show()
                }

                dialog.dismiss()
            }
        }
        addDialog.setNegativeButton("Cancel"){
                dialog,_->
            dialog.dismiss()
        }
        addDialog.create()
        addDialog.show()
    }

    private fun deleteRecord(id: String, name: String) {
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("member").child(id)

        reference.removeValue().addOnSuccessListener {
            Toast.makeText(this, "$name deleted successfully!", Toast.LENGTH_LONG).show()
        }.addOnFailureListener { error ->
            Toast.makeText(this, "Cannot delete $name. Error : ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun isValidPhoneNumber(number: String): Boolean {
        val pattern: Pattern = Patterns.PHONE
        return pattern.matcher(number).matches()
    }

    private fun isOnlyLetters(word: String): Boolean {
        val regex = "^[A-Za-z]*$".toRegex()
        return regex.matches(word)
    }
}

