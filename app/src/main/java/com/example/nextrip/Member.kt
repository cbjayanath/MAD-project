package com.example.nextrip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

class Member : AppCompatActivity() {
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
            memberList.add(MemberData("Name: $name","Mobile No. : $number"))
            memberAdapter.notifyDataSetChanged()
            Toast.makeText(this,"Member Added!",Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        addDialog.setNegativeButton("Cancel"){
                dialog,_->
            dialog.dismiss()
        }
        addDialog.create()
        addDialog.show()
    }
}