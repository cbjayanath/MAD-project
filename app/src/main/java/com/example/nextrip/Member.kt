package com.example.nextrip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nextrip.Adapters.MembersAdapter
import com.example.nextrip.model.MemberData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*
import java.util.regex.Pattern

class Member : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    private lateinit var addsBtn: FloatingActionButton
    private lateinit var memberRecyclerView: RecyclerView
    private lateinit var memberList: ArrayList<MemberData>
    private lateinit var membersAdapter: MembersAdapter
    //private lateinit var loadicon: ImageView

    private lateinit var btnmember: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member)

        btnmember = findViewById(R.id.memberNextBtn)

        addsBtn = findViewById(R.id.addingButton)

        memberRecyclerView = findViewById(R.id.mRecycler)
        memberRecyclerView.layoutManager = LinearLayoutManager(this)
        memberRecyclerView.setHasFixedSize(true)


//        loadicon = findViewById(R.id.image_loadicon)
//        Glide.with(this).load(R.drawable.loadicon).into(loadicon)

        memberList = arrayListOf<MemberData>()

        showMemberData()

        addsBtn.setOnClickListener{
            addInfo()
        }

        btnmember.setOnClickListener{
            val intent = Intent(this,Backpack::class.java)
            startActivity(intent)
        }

    }

    private fun showMemberData() {

        memberRecyclerView.visibility = View.GONE
        //loadicon.visibility = View.VISIBLE

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("member")

        reference.orderByChild("tripid").equalTo(intent.getStringExtra("tripid").toString()).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                memberList.clear()

                if (snapshot.exists()) {
                    for (ms in snapshot.children) {
                        val memberData = ms.getValue(MemberData::class.java)
                        if (memberData != null) {
                            memberList.add(memberData)
                        }
                    }

                    membersAdapter = MembersAdapter(memberList)
                    memberRecyclerView.adapter = membersAdapter
                    memberRecyclerView.visibility = View.VISIBLE
                    //loadicon.visibility = View.GONE

                    membersAdapter.setonItemClickListener(object : MembersAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val memberDetailsIntent = Intent(this@Member, MemberDetail::class.java)


                            memberDetailsIntent.putExtra("memberphonenumber", memberList[position].memberMobile)
                            memberDetailsIntent.putExtra("membername", memberList[position].memberName)
                            memberDetailsIntent.putExtra("memberaddress", memberList[position].memberAddress)
                            memberDetailsIntent.putExtra("memberemcontact", memberList[position].memberEmergencyNumber)
                            memberDetailsIntent.putExtra("tripid", memberList[position].tripid)

                            startActivity(memberDetailsIntent)
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

                reference.child(number).setValue(member).addOnCompleteListener{
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

    private fun isValidPhoneNumber(number: String): Boolean {
        val pattern: Pattern = Patterns.PHONE
        return pattern.matcher(number).matches()
    }

    private fun isOnlyLetters(word: String): Boolean {
        val regex = "^[A-Za-z]*$".toRegex()
        return regex.matches(word)
    }
}