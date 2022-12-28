package com.example.nextrip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.nextrip.model.ItemData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.temporal.TemporalAccessor
import java.util.*

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

    private var isRented: String ?= null

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

        showItemDetails()

        btnshare.setOnClickListener{
            shareItem(intent.getStringExtra("itemname").toString(), intent.getStringExtra("itemquantity").toString(), intent.getStringExtra("itemdesc").toString())
        }

        btnedit.setOnClickListener{
            updateBackpackInfo()
        }

        btndelete.setOnClickListener{
            deleteRecord(intent.getStringExtra("itemid").toString(), intent.getStringExtra("itemname").toString())
        }

        btnback.setOnClickListener {
            back()
        }

    }

    private fun showItemDetails() {

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

    private fun updateBackpackInfo() {

        val inflter = LayoutInflater.from(this)
        val v = inflter.inflate(R.layout.add_backpack,null)
        /**set view*/
        val itemName = v.findViewById<TextInputEditText>(R.id.bName)
        val itemQuantity = v.findViewById<TextInputEditText>(R.id.bQuantity)
        val itemDescription=v.findViewById<TextInputEditText>(R.id.bDescription)

        val addDialog = AlertDialog.Builder(this)
        addDialog.setView(v)
        addDialog.setPositiveButton("Add"){
                dialog,_->

            val name = itemName.text?.trim().toString()
            val quantity = itemQuantity.text?.trim().toString().toInt()
            val desc = itemDescription.text?.trim().toString()

            if(name.isEmpty()){
                Toast.makeText(this, "Name field is required!", Toast.LENGTH_LONG).show()
            }else if(quantity.toString().isEmpty()){
                Toast.makeText(this, "quantity is required!", Toast.LENGTH_LONG).show()
            }else if(!isOnlyNumbers(quantity.toString())){
                Toast.makeText(this, "Give quantity as number!", Toast.LENGTH_LONG).show()
            }else if(desc.isEmpty()){
                Toast.makeText(this, "Description is required!", Toast.LENGTH_LONG).show()
            }else{

                database = FirebaseDatabase.getInstance()
                reference = database.getReference("item")

                val itemid = intent.getStringExtra("itemid").toString()

                val words = desc.lowercase(Locale.getDefault()).split("\\s+".toRegex())

                val isrent: Boolean = "rent" in words
                val isrented: Boolean = "rented" in words

                val isfriend: Boolean = "friend" in words
                val ismemeber: Boolean = "member" in words
                val ismr: Boolean = "mr" in words

                if(isrent || isrented){
                    setRented("A rented item*")
                }else if(isfriend || ismemeber || ismr){
                    setRented("One member item")
                }else{
                    setRented("One of my item")
                }

                val item = ItemData(itemid, name, quantity, desc, getRented(), intent.getStringExtra("tripid").toString())

                reference.child(itemid).setValue(item).addOnCompleteListener{
                    if(it.isSuccessful){
                        if(getRented()=="A rented item*"){
                            Toast.makeText(this,"Rented $name Updated Successfully!", Toast.LENGTH_LONG).show()
                        }else if(getRented()=="One member item"){
                            Toast.makeText(this,"One members $name Updated Successfully!", Toast.LENGTH_LONG).show()
                        }else if(getRented()=="One of my item"){
                            Toast.makeText(this,"Your $name Updated Successfully!", Toast.LENGTH_LONG).show()
                        }else{

                        }
                    }
                }.addOnFailureListener {
                    Toast.makeText(this,"Cannot add $name", Toast.LENGTH_LONG).show()
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

    private fun shareItem(name: String ?= null, quantity: String ?= null, desc: String ?= null) {

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

    private fun isOnlyNumbers(q: String): Boolean {
        val regex = "^[0-9]*$".toRegex()
        return regex.matches(q)
    }

    private fun back(){
        val backIntent = Intent(this@BackpackItemDetails, Backpack::class.java)
        backIntent.putExtra("tripid", intent.getStringExtra("tripid")).toString()
        startActivity(backIntent)
    }

    private fun setRented(r: String?){
        this.isRented = r
    }

    private fun getRented(): String? {
        return isRented
    }
}