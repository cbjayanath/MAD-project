package com.example.nextrip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nextrip.Adapters.BackpackAdapter
import com.example.nextrip.Adapters.MembersAdapter
import com.example.nextrip.model.ItemData
import com.example.nextrip.model.MemberData
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*
import java.util.*

class Backpack : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    private lateinit var addbtn: FloatingActionButton
    private lateinit var backpackRecyclerView: RecyclerView
    private lateinit var itemList: ArrayList<ItemData>
    private lateinit var backpackAdapter: BackpackAdapter

    private lateinit var backpackNextBtn: MaterialButton

    private var isRented: String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_backpack)

        addbtn = findViewById(R.id.item_btn_add)
        backpackNextBtn = findViewById(R.id.backpackNextBtn)

        backpackRecyclerView = findViewById(R.id.bRecycler)
        backpackRecyclerView.layoutManager = LinearLayoutManager(this)
        backpackRecyclerView.setHasFixedSize(true)

//        loadicon = findViewById(R.id.image_loadicon)
//        Glide.with(this).load(R.drawable.loadicon).into(loadicon)

        itemList = arrayListOf<ItemData>()

        showItemData()

        addbtn.setOnClickListener{
            addInfo()
        }

        backpackNextBtn.setOnClickListener{
            val backpackIntent = Intent(this,Location::class.java)
            backpackIntent.putExtra("tripid", intent.getStringExtra("tripid").toString())
            startActivity(backpackIntent)
        }
    }

    private fun showItemData() {
        backpackRecyclerView.visibility = View.GONE
        //loadicon.visibility = View.VISIBLE

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("item")

        reference.orderByChild("tripid").equalTo(intent.getStringExtra("tripid").toString()).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                itemList.clear()
                if(snapshot.exists()){
                    for(li in snapshot.children){
                        val itemData = li.getValue(ItemData::class.java)
                        if (itemData != null) {
                            itemList.add(itemData)
                        }
                    }

                    backpackAdapter = BackpackAdapter(itemList)
                    backpackRecyclerView.adapter = backpackAdapter
                    backpackRecyclerView.visibility = View.VISIBLE

                    backpackAdapter.setonItemClickListener(object : BackpackAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val itemDetailsIntent = Intent(this@Backpack, BackpackItemDetails::class.java)

                            itemDetailsIntent.putExtra("itemid", itemList[position].itemid)
                            itemDetailsIntent.putExtra("itemname", itemList[position].name)
                            itemDetailsIntent.putExtra("itemquantity", itemList[position].quantity)
                            itemDetailsIntent.putExtra("itemdesc", itemList[position].description)
                            itemDetailsIntent.putExtra("itemrented", itemList[position].rented)
                            itemDetailsIntent.putExtra("tripid", itemList[position].tripid)

                            startActivity(itemDetailsIntent)
                        }

                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun addInfo() {

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

                val itemid = reference.push().key!!

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
                            Toast.makeText(this,"Rented $name Added Successfully!", Toast.LENGTH_LONG).show()
                        }else if(getRented()=="One member item"){
                            Toast.makeText(this,"One members $name Added Successfully!", Toast.LENGTH_LONG).show()
                        }else if(getRented()=="One of my item"){
                            Toast.makeText(this,"Your $name Added Successfully!", Toast.LENGTH_LONG).show()
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

    private fun isOnlyNumbers(q: String): Boolean {
        val regex = "^[0-9]*$".toRegex()
        return regex.matches(q)
    }

    private fun setRented(r: String?){
        this.isRented = r
    }

    private fun getRented(): String? {
        return isRented
    }

}
