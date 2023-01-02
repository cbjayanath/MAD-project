package com.example.nextrip

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nextrip.Adapters.BackpackAdapter
import com.example.nextrip.Adapters.EmeragancyAdapter
import com.example.nextrip.model.EmergancyData
import com.example.nextrip.model.ItemData
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Emergancy : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var emrgenacyRecyclerView: RecyclerView
    private lateinit var msgList: ArrayList<EmergancyData>
    private lateinit var emeragancyAdapter: EmeragancyAdapter

    private lateinit var btnback: ImageView

    private lateinit var msgsendBtn: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergancy)

        msgsendBtn = findViewById(R.id.emSendBtn)
        btnback = findViewById(R.id.em_trip_btn_back)

        emrgenacyRecyclerView = findViewById(R.id.emRecycler)
        emrgenacyRecyclerView.layoutManager = LinearLayoutManager(this)
        emrgenacyRecyclerView.setHasFixedSize(true)

//        loadicon = findViewById(R.id.image_loadicon)
//        Glide.with(this).load(R.drawable.loadicon).into(loadicon)

        msgList = arrayListOf<EmergancyData>()

        showItemData()

        msgsendBtn.setOnClickListener {
            addInfo()
        }

        btnback.setOnClickListener {
            back()
        }
    }

    private fun showItemData() {
        emrgenacyRecyclerView.visibility = View.GONE
        //loadicon.visibility = View.VISIBLE

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("emergancy")

        reference.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                msgList.clear()
                if(snapshot.exists()){
                    for(li in snapshot.children){
                        val emData = li.getValue(EmergancyData::class.java)
                        if (emData != null) {
                            msgList.add(emData)
                        }
                    }

                    emeragancyAdapter = EmeragancyAdapter(msgList)
                    emrgenacyRecyclerView.adapter = emeragancyAdapter
                    emrgenacyRecyclerView.visibility = View.VISIBLE

                    emeragancyAdapter.setonItemClickListener(object : EmeragancyAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val gmmIntentUri = Uri.parse("geo:${msgList[position].latitude},${msgList[position].longitude}")
                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                            mapIntent.setPackage("com.google.android.apps.maps")
                            startActivity(mapIntent)

                        }

                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addInfo() {

        val inflter = LayoutInflater.from(this)
        val v = inflter.inflate(R.layout.add_em_msg,null)
        /**set view*/
        val emservicemsg = v.findViewById<TextInputEditText>(R.id.emservice_msg)

        val addDialog = AlertDialog.Builder(this)
        addDialog.setView(v)
        addDialog.setPositiveButton("Add"){
                dialog,_->

            val msg = emservicemsg.text?.trim().toString()

            if(msg.isEmpty()){
                Toast.makeText(this, "Message field is required!", Toast.LENGTH_LONG).show()
            }else{

                database = FirebaseDatabase.getInstance()
                reference = database.getReference("emergancy")

                firebaseAuth = FirebaseAuth.getInstance()

                val emid = reference.push().key!!

                val date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MMMM/yyyy"))
                val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh.mm a"))

                val emergancyData = EmergancyData(emid, firebaseAuth.currentUser?.uid.toString() , msg, intent.getStringExtra("lati").toString(), intent.getStringExtra("long").toString(), intent.getStringExtra("locationdetails").toString(), date, time, intent.getStringExtra("tripid").toString())

                reference.child(emid).setValue(emergancyData).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this,"Your message sent successfully with your location!", Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this,"Cannot add $msg", Toast.LENGTH_LONG).show()
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

    private fun back(){
        val backIntent = Intent(this@Emergancy, MainMenu::class.java)
        backIntent.putExtra("tripid", intent.getStringExtra("tripid")).toString()
        startActivity(backIntent)
    }
}