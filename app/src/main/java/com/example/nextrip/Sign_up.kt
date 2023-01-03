package com.example.nextrip

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nextrip.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.regex.Pattern

class Sign_up : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var txt_firstname : EditText
    private lateinit var txt_lastname : EditText
    private lateinit var txt_email : EditText
    private lateinit var pw_password : EditText

    private lateinit var btnsignup : Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        txt_firstname = findViewById(R.id.txt_input_signup_firstname)!!
        txt_lastname = findViewById(R.id.txt_input_signup_lastname)!!
        txt_email = findViewById(R.id.txt_input_signup_email)!!
        pw_password = findViewById(R.id.txt_input_signup_password)!!

        btnsignup = findViewById(R.id.btn_signup)

        btnsignup.setOnClickListener{

            signup()
        }
    }

    private fun signup() {


        val firstname = txt_firstname.text?.trim().toString()
        val lastname = txt_lastname.text?.trim().toString()
        val email = txt_email.text?.trim().toString()
        val password = pw_password.text?.trim().toString()

        if(firstname.isEmpty()){
            Toast.makeText(this, "First Name field is required!", Toast.LENGTH_LONG).show()
        }else if(lastname.isEmpty()){
            Toast.makeText(this, "Last Name field is required!", Toast.LENGTH_LONG).show()
        }else if(email.isEmpty()){
            Toast.makeText(this, "Email field is required!", Toast.LENGTH_LONG).show()
        }else if(password.isEmpty()){
            Toast.makeText(this, "Password is required!", Toast.LENGTH_LONG).show()
        }else if(password.length < 6){
            Toast.makeText(this, "Password is not strong!", Toast.LENGTH_LONG).show()
        }else if (!isOnlyLetters(firstname)){
            Toast.makeText(this, "First Name is not valid!", Toast.LENGTH_LONG).show()
        }else if (!isOnlyLetters(lastname)){
            Toast.makeText(this, "Last Name is not valid!", Toast.LENGTH_LONG).show()
        }else if (!isValidEmail(email)){
            Toast.makeText(this, "Email is not valid!", Toast.LENGTH_LONG).show()
        }else{

            firebaseAuth = FirebaseAuth.getInstance()

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { it ->
                if(it.isSuccessful){

                    database = FirebaseDatabase.getInstance()
                    reference = database.getReference("user")

                    val userid = firebaseAuth.currentUser?.uid.toString()

                    val user = UserData(userid, firstname, lastname, email, password)

                    reference.child(userid).setValue(user)
                        .addOnCompleteListener{

                            if(it.isSuccessful){
                                val userDetailsIntent = Intent(this@Sign_up, WalkThrough_Slider::class.java)

                                userDetailsIntent.putExtra("firstname_from_signup", firstname)
                                userDetailsIntent.putExtra("lastname_from_signup", lastname)
                                userDetailsIntent.putExtra("email_from_signup", email)

                                startActivity(userDetailsIntent)

                                Toast.makeText(this, "You are registered!", Toast.LENGTH_LONG).show()
                            }
                        }
                        .addOnFailureListener{ error ->
                            Toast.makeText(this, "error ${error.message}", Toast.LENGTH_LONG).show()
                        }
                }
            }.addOnCanceledListener {
                Toast.makeText(this, "Sign up failed!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()

    }

    private fun isOnlyLetters(word: String): Boolean {
        val regex = "^[A-Za-z]*$".toRegex()
        return regex.matches(word)
    }
}