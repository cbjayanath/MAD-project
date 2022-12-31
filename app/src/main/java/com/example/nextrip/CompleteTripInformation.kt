package com.example.nextrip

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.Executor

class CompleteTripInformation : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    private lateinit var btnback: ImageView
    private lateinit var name: TextView
    private lateinit var description: TextView
    private lateinit var datetime: TextView

    private lateinit var btnmembers: FloatingActionButton
    private lateinit var btnbackpack: FloatingActionButton
    private lateinit var btnlocation: FloatingActionButton

    private lateinit var executer: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var btndelete: Button
    private lateinit var btndeleteconfirm: Button

    private var tripstartdate: String ?= null
    private var tripenddate: String ?= null
    private var tripendtime: String ?= null

    private var isConfirmBtnVisible: String ?= null

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_trip_information)

        btnback = findViewById(R.id.details_trip_btn_back)

        btndelete = findViewById(R.id.deleteTripBtn)
        btndeleteconfirm = findViewById(R.id.deleteTripConfirmBtn)

        name = findViewById(R.id.trip_txt_show_name)
        description = findViewById(R.id.trip_txt_show_description)
        datetime = findViewById(R.id.trip_txt_show_datetime)

        btnmembers = findViewById(R.id.details_trip_btn_members)
        btnbackpack = findViewById(R.id.details_trip_btn_backpack)
        btnlocation = findViewById(R.id.details_trip_btn_locations)

        tripstartdate = intent.getStringExtra("tripstartdate").toString()
        tripenddate = intent.getStringExtra("tripenddate").toString()
        tripendtime = intent.getStringExtra("tripendtime").toString()

        showTripInfo()

        executer = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executer, object  : BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(this@CompleteTripInformation, "line 34 error Auth", Toast.LENGTH_LONG).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)

                deleteThisTrip(intent.getStringExtra("tripid").toString())
                Toast.makeText(this@CompleteTripInformation, "Success!", Toast.LENGTH_LONG).show()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(this@CompleteTripInformation, "line 44 error Auth failed!", Toast.LENGTH_LONG).show()
            }
        })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Delete this trip")
            .setSubtitle("If you confirm this trip it's members, items, locations and all memories are gone and cannot view again! Are you sure you want to delete this trip?")
            .setNegativeButtonText("negative btn text")
            .build()

        btnback.setOnClickListener {
            back()
        }

        btnmembers.setOnClickListener {
            seeMembers()
        }

        btnbackpack.setOnClickListener {
            seeBackpack()
        }

        btnlocation.setOnClickListener {
            seeLocation()
        }

        btndelete.setOnClickListener {
            btndeleteconfirm.visibility = View.VISIBLE
            setVisible("yes")
        }

        btndeleteconfirm.setOnClickListener {
            if(getVisible()=="yes"){
                checkDeviceHasBiometric()
                biometricPrompt.authenticate(promptInfo)
            }
        }

    }

    private fun showTripInfo(){
        name.text = intent.getStringExtra("tripname").toString()
        description.text = intent.getStringExtra("tripdescription").toString()

        datetime.text = "Trip start on " + tripstartdate + " and ended on " + tripenddate + " at " + tripendtime
    }

    private fun seeMembers(){
        val backpackIntent = Intent(this@CompleteTripInformation,MemberCompleted::class.java)
        backpackIntent.putExtra("tripid", intent.getStringExtra("tripid").toString())
        backpackIntent.putExtra("tripname", intent.getStringExtra("tripname").toString())
        backpackIntent.putExtra("tripdescription", intent.getStringExtra("tripdescription").toString())
        backpackIntent.putExtra("tripstartdate", intent.getStringExtra("tripstartdate").toString())
        backpackIntent.putExtra("tripenddate", intent.getStringExtra("tripenddate").toString())
        backpackIntent.putExtra("tripendtime", intent.getStringExtra("tripendtime").toString())
        startActivity(backpackIntent)
    }

    private fun seeBackpack(){
        val backpackIntent = Intent(this@CompleteTripInformation,BackpackCompleted::class.java)
        backpackIntent.putExtra("tripid", intent.getStringExtra("tripid").toString())
        backpackIntent.putExtra("tripname", intent.getStringExtra("tripname").toString())
        backpackIntent.putExtra("tripdescription", intent.getStringExtra("tripdescription").toString())
        backpackIntent.putExtra("tripstartdate", intent.getStringExtra("tripstartdate").toString())
        backpackIntent.putExtra("tripenddate", intent.getStringExtra("tripenddate").toString())
        backpackIntent.putExtra("tripendtime", intent.getStringExtra("tripendtime").toString())
        startActivity(backpackIntent)
    }

    private fun seeLocation(){
        val backpackIntent = Intent(this@CompleteTripInformation,LocationCompleted::class.java)
        backpackIntent.putExtra("tripid", intent.getStringExtra("tripid").toString())
        backpackIntent.putExtra("tripname", intent.getStringExtra("tripname").toString())
        backpackIntent.putExtra("tripdescription", intent.getStringExtra("tripdescription").toString())
        backpackIntent.putExtra("tripstartdate", intent.getStringExtra("tripstartdate").toString())
        backpackIntent.putExtra("tripenddate", intent.getStringExtra("tripenddate").toString())
        backpackIntent.putExtra("tripendtime", intent.getStringExtra("tripendtime").toString())
        startActivity(backpackIntent)
    }

    private fun setVisible(v : String?){
        isConfirmBtnVisible = v
    }

    private fun getVisible(): String? {
        return isConfirmBtnVisible
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun checkDeviceHasBiometric() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.")

            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.e("MY_APP_TAG", "No biometric features available on this device.")

            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")

            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Prompts the user to create credentials that your app accepts.
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
                    )
                }

                startActivityForResult(enrollIntent, 100)
            }
        }
    }

    private fun deleteThisTrip(id: String){

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("trip").child(id)

        reference.removeValue().addOnSuccessListener {

            reference = database.getReference("member").child(id)

            reference.removeValue().addOnSuccessListener {

                reference = database.getReference("item").child(id)

                reference.removeValue().addOnSuccessListener {

                    reference = database.getReference("location").child(id)

                    reference.removeValue().addOnSuccessListener {
                        Toast.makeText(this, "Trip deleted successfully!", Toast.LENGTH_LONG).show()
                        back()
                    }.addOnFailureListener { error ->
                        Toast.makeText(this, "Cannot delete location. Error : ${error.message}", Toast.LENGTH_LONG).show()
                    }

                }.addOnFailureListener { error ->
                    Toast.makeText(this, "Cannot delete item. Error : ${error.message}", Toast.LENGTH_LONG).show()
                }

            }.addOnFailureListener { error ->
                Toast.makeText(this, "Cannot delete member. Error : ${error.message}", Toast.LENGTH_LONG).show()
            }

        }.addOnFailureListener { error ->
            Toast.makeText(this, "Cannot delete trip. Error : ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun back() {
        val backpackIntent = Intent(this@CompleteTripInformation,MyTrips::class.java)
        backpackIntent.putExtra("tripid", intent.getStringExtra("tripid").toString())
        startActivity(backpackIntent)
    }
}