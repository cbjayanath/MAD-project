package com.example.nextrip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nextrip.Adapters.MemberCompleteAdapter
import com.example.nextrip.Adapters.MembersAdapter
import com.example.nextrip.model.MemberData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class MemberCompleted : AppCompatActivity() {

    private lateinit var btnback: ImageView

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    private lateinit var memberRecyclerView: RecyclerView
    private lateinit var memberList: ArrayList<MemberData>
    private lateinit var membersAdapter: MemberCompleteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_completed)

        btnback = findViewById(R.id.cm_trip_btn_back)

        memberRecyclerView = findViewById(R.id.cMRecycler)
        memberRecyclerView.layoutManager = LinearLayoutManager(this)
        memberRecyclerView.setHasFixedSize(true)

//        loadicon = findViewById(R.id.image_loadicon)
//        Glide.with(this).load(R.drawable.loadicon).into(loadicon)

        memberList = arrayListOf<MemberData>()

        btnback.setOnClickListener {
            back()
        }

        showMemberData()
    }

    private fun showMemberData() {

        memberRecyclerView.visibility = View.GONE
        //loadicon.visibility = View.VISIBLE

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("member")

        reference.orderByChild("tripid").equalTo(intent.getStringExtra("tripid").toString()).addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                memberList.clear()

                if (snapshot.exists()) {
                    for (ms in snapshot.children) {
                        val memberData = ms.getValue(MemberData::class.java)
                        if (memberData != null) {
                            memberList.add(memberData)
                        }
                    }

                    membersAdapter = MemberCompleteAdapter(memberList)

                    memberRecyclerView.adapter = membersAdapter
                    memberRecyclerView.visibility = View.VISIBLE
                    //loadicon.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun back() {
        val backpackIntent = Intent(this@MemberCompleted,CompleteTripInformation::class.java)
        backpackIntent.putExtra("tripid", intent.getStringExtra("tripid").toString())
        backpackIntent.putExtra("tripname", intent.getStringExtra("tripname").toString())
        backpackIntent.putExtra("tripdescription", intent.getStringExtra("tripdescription").toString())
        backpackIntent.putExtra("tripstartdate", intent.getStringExtra("tripstartdate").toString())
        backpackIntent.putExtra("tripenddate", intent.getStringExtra("tripenddate").toString())
        backpackIntent.putExtra("tripendtime", intent.getStringExtra("tripendtime").toString())
        startActivity(backpackIntent)
    }
}