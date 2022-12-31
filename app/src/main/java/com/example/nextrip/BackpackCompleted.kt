package com.example.nextrip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nextrip.Adapters.BackpackAdapter
import com.example.nextrip.Adapters.BackpackCompleteAdapter
import com.example.nextrip.model.ItemData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import java.util.ArrayList

class BackpackCompleted : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    private lateinit var backpackRecyclerView: RecyclerView
    private lateinit var itemList: ArrayList<ItemData>
    private lateinit var backpackAdapter: BackpackCompleteAdapter

    private lateinit var btnback: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_backpack_completed)

        btnback = findViewById(R.id.cb_trip_btn_back)

        backpackRecyclerView = findViewById(R.id.cBRecycler)
        backpackRecyclerView.layoutManager = LinearLayoutManager(this)
        backpackRecyclerView.setHasFixedSize(true)

//        loadicon = findViewById(R.id.image_loadicon)
//        Glide.with(this).load(R.drawable.loadicon).into(loadicon)

        itemList = arrayListOf<ItemData>()

        showItemData()

        btnback.setOnClickListener {
            back()
        }

    }

    private fun showItemData() {
        backpackRecyclerView.visibility = View.GONE
        //loadicon.visibility = View.VISIBLE

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("item")

        reference.orderByChild("tripid").equalTo(intent.getStringExtra("tripid").toString()).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                itemList.clear()
                if(snapshot.exists()){
                    for(li in snapshot.children){
                        val itemData = li.getValue(ItemData::class.java)
                        if (itemData != null) {
                            itemList.add(itemData)
                        }
                    }

                    backpackAdapter = BackpackCompleteAdapter(itemList)
                    backpackRecyclerView.adapter = backpackAdapter
                    backpackRecyclerView.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun back() {
        val backpackIntent = Intent(this@BackpackCompleted,CompleteTripInformation::class.java)
        backpackIntent.putExtra("tripid", intent.getStringExtra("tripid").toString())
        backpackIntent.putExtra("tripname", intent.getStringExtra("tripname").toString())
        backpackIntent.putExtra("tripdescription", intent.getStringExtra("tripdescription").toString())
        backpackIntent.putExtra("tripstartdate", intent.getStringExtra("tripstartdate").toString())
        backpackIntent.putExtra("tripenddate", intent.getStringExtra("tripenddate").toString())
        backpackIntent.putExtra("tripendtime", intent.getStringExtra("tripendtime").toString())
        startActivity(backpackIntent)
    }
}