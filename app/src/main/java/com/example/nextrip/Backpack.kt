package com.example.nextrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog
import com.example.nextrip.model.ItemData
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Backpack : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    private lateinit var addbtn: FloatingActionButton

    private lateinit var toggle: MaterialButtonToggleGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_backpack)

        addbtn = findViewById(R.id.item_btn_add)

        addbtn.setOnClickListener{
            addInfo()
        }
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

                toggle = findViewById(R.id.btntoggle_isRented)!!

                database = FirebaseDatabase.getInstance()
                reference = database.getReference("item")

                val itemid = reference.push().key!!

                 toggle.addOnButtonCheckedListener { _, checkedId, isChecked ->
                     if(isChecked){
                         when(checkedId){
                             R.id.isRented_item_btn_yes ->{
                                 Toast.makeText(this, "yes!", Toast.LENGTH_LONG).show()

                                 val item = ItemData(itemid, name, quantity, desc, true, intent.getStringExtra("tripid").toString())

                                 reference.child(itemid).setValue(item).addOnCompleteListener{
                                     if(it.isSuccessful){
                                         Toast.makeText(this,"$name Added Successfully!", Toast.LENGTH_LONG).show()
                                     }
                                 }.addOnFailureListener {
                                     Toast.makeText(this,"Cannot add $name", Toast.LENGTH_LONG).show()
                                 }

                                 dialog.dismiss()
                             }

                             R.id.isRented_item_btn_no ->{
                                 Toast.makeText(this, "no", Toast.LENGTH_LONG).show()

                                 val item = ItemData(itemid, name, quantity, desc, false, intent.getStringExtra("tripid").toString())

                                 reference.child(itemid).setValue(item).addOnCompleteListener{
                                     if(it.isSuccessful){
                                         Toast.makeText(this,"$name Added Successfully!", Toast.LENGTH_LONG).show()
                                     }
                                 }.addOnFailureListener {
                                     Toast.makeText(this,"Cannot add $name", Toast.LENGTH_LONG).show()
                                 }

                                 dialog.dismiss()
                             }
                         }
                     }
                 }

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
}