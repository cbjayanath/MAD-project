package com.example.nextrip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nextrip.model.MemberData
import com.example.nextrip.view.MemberAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.regex.Pattern

class Member : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    private lateinit var addsBtn: FloatingActionButton
    private lateinit var recv: RecyclerView
    private lateinit var memberList:ArrayList<MemberData>
    private lateinit var memberAdapter:MemberAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member)
        var nextbtn=findViewById<Button>(R.id.memberNextBtn)
        memberList = ArrayList()/**set List*/

        /**set find Id*/
        addsBtn = findViewById(R.id.addingButton)
        recv = findViewById(R.id.mRecycler)

        memberAdapter = MemberAdapter(this, memberList) /**set Adapter*/
        /**setRecycler view Adapter*/
        recv.layoutManager = LinearLayoutManager(this)
        recv.adapter = memberAdapter

        addsBtn.setOnClickListener{
            addInfo()
        }

        nextbtn.setOnClickListener{
            startActivity(Intent(this,Backpack::class.java))
        }

    }

    private fun addInfo() {

        val inflter = LayoutInflater.from(this)
        val v = inflter.inflate(R.layout.add_member,null)
        /**set view*/
        val memberName = v.findViewById<TextInputEditText>(R.id.mName)
        val memberTel = v.findViewById<TextInputEditText>(R.id.mTelephone)
        val memberEmergency=v.findViewById<TextInputEditText>(R.id.mEmergency)
        val memberAddress=v.findViewById<TextInputEditText>(R.id.mAddress)

        val addDialog = AlertDialog.Builder(this)
        addDialog.setView(v)
        addDialog.setPositiveButton("Add"){
                dialog,_->

            val name = memberName.text.toString()
            val number = memberTel.text.toString()
            val emergency=memberEmergency.text.toString()
            val address=memberAddress.text.toString()

            if(name.isEmpty()){
                Toast.makeText(this, "Name field is required!", Toast.LENGTH_LONG).show()
            }else if(!isOnlyLetters(name)){
                Toast.makeText(this, "Invalid name! Please give first name", Toast.LENGTH_LONG).show()
            }else if(number.isEmpty()){
                Toast.makeText(this, "Phone number is required!", Toast.LENGTH_LONG).show()
            }else if(!isValidPhoneNumber(number)){
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

                reference.child(number).setValue(member).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this,"$name Added Successfully!",Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this,"Cannot add $name",Toast.LENGTH_LONG).show()
                }

                //memberAdapter.notifyDataSetChanged()

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

    private fun isValidPhoneNumber(number: String): Boolean {
        val pattern: Pattern = Patterns.PHONE
        return pattern.matcher(number).matches()
    }

    private fun isOnlyLetters(word: String): Boolean {
        val regex = "^[A-Za-z]*$".toRegex()
        return regex.matches(word)
    }
}