package com.example.nextrip

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CompleteTrip : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    private lateinit var backtomainmenu: Button

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_trip)

        backtomainmenu = findViewById(R.id.backToTheMainMenu)

        backtomainmenu.setOnClickListener {
            markAsEnd(intent.getStringExtra("tripid").toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun markAsEnd(id: String){

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("trip").child(id)

        val endDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MMMM/yyyy"))
        val endTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh.mm a"))

        reference.child("end").setValue("yes").addOnCompleteListener{
            if(it.isSuccessful){

                reference.child("endDate").setValue(endDate).addOnCompleteListener{
                    if(it.isSuccessful){

                        reference.child("endTime").setValue(endTime).addOnCompleteListener{
                            if(it.isSuccessful){
                                Toast.makeText(this, "Your trip completed successfully!", Toast.LENGTH_LONG).show()
                                back()
                            }
                        }.addOnFailureListener {
                            Toast.makeText(this, "Cannot mark as end because end time cannot insert!", Toast.LENGTH_LONG).show()
                        }

                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Cannot mark as end because end date cannot insert!", Toast.LENGTH_LONG).show()
                }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Cannot complete trip!", Toast.LENGTH_LONG).show()
        }
    }

    private fun back(){
        val backIntent = Intent(this@CompleteTrip, MainActivity::class.java)
        startActivity(backIntent)
    }
}