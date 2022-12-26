package com.example.nextrip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.temporal.TemporalAccessor

class BackpackItemDetails : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    private lateinit var btnback: ImageView

    private lateinit var name: TextView
    private lateinit var quantity: TextView
    private lateinit var desc: TextView
    private lateinit var about: TextView

    private lateinit var imgrent: ImageView
    private lateinit var imgfriend: ImageView
    private lateinit var imgmy: ImageView

    private lateinit var btnshare: FloatingActionButton
    private lateinit var btnedit: FloatingActionButton
    private lateinit var btndelete: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_backpack_item_details)

        btnback = findViewById(R.id.item_details_btn_back)

        name = findViewById(R.id.item_details_txt_show_name)
        quantity = findViewById(R.id.item_details_txt_show_quantity)
        desc = findViewById(R.id.item_details_txt_desc)
        about = findViewById(R.id.item_details_txt_show_about)

        imgrent = findViewById(R.id.item_details_img_rented)
        imgfriend = findViewById(R.id.item_details_img_friend_member)
        imgmy = findViewById(R.id.item_details_img_my)

        btnshare = findViewById(R.id.details_item_btn_share)
        btnedit = findViewById(R.id.details_item_btn_update)
        btndelete = findViewById(R.id.details_item_btn_delete)

        showMemberDetails()

        btnshare.setOnClickListener{
            shareMember(intent.getStringExtra("itemname").toString(), intent.getStringExtra("itemquantity").toString(), intent.getStringExtra("itemdesc").toString())
        }

        btndelete.setOnClickListener{
            deleteRecord(intent.getStringExtra("itemid").toString(), intent.getStringExtra("itemname").toString())
        }

        btnback.setOnClickListener {
            back()
        }

    }

    private fun showMemberDetails() {

        if(intent.getStringExtra("itemrented").toString() == "A rented item*"){
            imgrent.visibility = View.VISIBLE
        }else if(intent.getStringExtra("itemrented").toString() == "One member item"){
            imgfriend.visibility = View.VISIBLE
        }else if(intent.getStringExtra("itemrented").toString() == "One of my item"){
            imgmy.visibility = View.VISIBLE
        }else{
            imgfriend.visibility = View.INVISIBLE
            imgrent.visibility = View.INVISIBLE
            imgmy.visibility = View.INVISIBLE
        }

        about.text = intent.getStringExtra("itemrented").toString()
        name.text = intent.getStringExtra("itemname").toString()
        quantity.text = intent.getStringExtra("itemquantity").toString()
        desc.text = intent.getStringExtra("itemdesc").toString()
    }

    private fun shareMember(name: String ?= null, quantity: String ?= null, desc: String ?= null) {

        val itemDetail = "THis item is $name and $desc"

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

    private fun deleteRecord(id: String, name: String) {
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("item").child(id)

        reference.removeValue().addOnSuccessListener {
            Toast.makeText(this, "$name deleted successfully!", Toast.LENGTH_LONG).show()
            back()
        }.addOnFailureListener { error ->
            Toast.makeText(this, "Cannot delete $name. Error : ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun back(){
        val backIntent = Intent(this@BackpackItemDetails, Backpack::class.java)
        backIntent.putExtra("tripid", intent.getStringExtra("tripid")).toString()
        startActivity(backIntent)
    }
}