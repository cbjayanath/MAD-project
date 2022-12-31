package com.example.nextrip

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter

class LocationShare : AppCompatActivity() {

    private lateinit var btnback: ImageView
    private lateinit var locationQR: ImageView
    private lateinit var btnshare: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_share)

        btnshare = findViewById(R.id.sharehBtn)
        locationQR = findViewById(R.id.location_details_share_show_img_qrcode)
        btnback = findViewById(R.id.location_details_share_btn_back)

        showLocationQR()

        btnback.setOnClickListener {
            back()
        }

        btnshare.setOnClickListener{
            shareLocation(intent.getStringExtra("locationname").toString(), intent.getStringExtra("locationcity").toString(), intent.getStringExtra("locationdistrict").toString(), intent.getStringExtra("locationdescription").toString())
        }
    }

    private fun showLocationQR() {

        val qr = intent.getStringExtra("locationname").toString() + " in " + intent.getStringExtra("locationcity").toString()

        if(qr.isEmpty()){
            locationQR.visibility = View.GONE
        }else if(qr.isNotEmpty()){

            val writer = QRCodeWriter()

            try {
                val bitMatrix = writer.encode(qr, BarcodeFormat.QR_CODE, 100, 100)
                val width = bitMatrix.width
                val height = bitMatrix.height
                val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

                for(x in 0 until width){
                    for(y in 0 until height){
                        bmp.setPixel(x,y, if(bitMatrix[x,y]) Color.BLUE else Color.WHITE)
                    }
                }

                locationQR.setImageBitmap(bmp)

            }catch (ex: WriterException){
                locationQR.visibility = View.INVISIBLE
                Toast.makeText(this, "QR automatically hide because of $ex", Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show()
        }
    }

    private fun shareLocation(name: String ?= null, city: String ?= null, district: String ?= null, desc: String ?= null) {

        val itemDetail = "$name located near to the $city $district. So, $desc"

        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, itemDetail)
        intent.type = "text/plain"

        if(itemDetail.isNotEmpty()){
            startActivity(Intent.createChooser(intent, "Share To : "))
            if(isFinishing){
                Toast.makeText(this, "$name shared successfully!", Toast.LENGTH_LONG).show()
            }
        }else if (itemDetail.isEmpty()){
            Toast.makeText(this, "Null content cannot share!", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this, "Cannot share $name!", Toast.LENGTH_LONG).show()
        }
    }

    private fun back(){
        val backIntent = Intent(this@LocationShare, LocationDetails::class.java)
        backIntent.putExtra("tripid", intent.getStringExtra("tripid")).toString()
        backIntent.putExtra("locationid", intent.getStringExtra("locationid")).toString()
        backIntent.putExtra("locationname", intent.getStringExtra("locationname")).toString()
        backIntent.putExtra("locationcity", intent.getStringExtra("locationcity")).toString()
        backIntent.putExtra("locationdistrict", intent.getStringExtra("locationdistrict")).toString()
        backIntent.putExtra("locationdescription", intent.getStringExtra("locationdescription")).toString()
        backIntent.putExtra("locationaddeddate", intent.getStringExtra("locationaddeddate")).toString()
        backIntent.putExtra("locationaddedtime", intent.getStringExtra("locationaddedtime")).toString()
        backIntent.putExtra("locationarrivaldate", intent.getStringExtra("locationarrivaldate")).toString()
        backIntent.putExtra("locationarrivaltime", intent.getStringExtra("locationarrivaltime")).toString()
        backIntent.putExtra("complete", intent.getStringExtra("complete")).toString()
        backIntent.putExtra("completeddate", intent.getStringExtra("completeddate")).toString()
        backIntent.putExtra("completedtime", intent.getStringExtra("completedtime")).toString()
        startActivity(backIntent)
    }
}