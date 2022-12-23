package com.example.nextrip

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.nextrip.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.regex.Pattern

class Sign_In : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var firebaseAuth : FirebaseAuth

    private lateinit var txt_email : EditText
    private lateinit var pw_password : EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        txt_email = findViewById(R.id.txt_input_signin_email)!!
        pw_password = findViewById(R.id.txt_input_signin_password)!!

        val signin_btn=findViewById<Button>(R.id.signInBtn)
        val signup_txt=findViewById<TextView>(R.id.signUpHereText)

        signin_btn.setOnClickListener{

            signIN()
        }

        signup_txt.setOnClickListener{
            startActivity(Intent(this,Sign_up::class.java))
        }
    }

    private fun signIN() {

        val email = txt_email.text.trim().toString()
        val password = pw_password.text.trim().toString()

        if(email.isEmpty()){
            Toast.makeText(this, "Email field is required!", Toast.LENGTH_LONG).show()
        }else if(password.isEmpty()){
            Toast.makeText(this, "Password is required!", Toast.LENGTH_LONG).show()
        }else if(password.length < 6){
            Toast.makeText(this, "Password is not strong!", Toast.LENGTH_LONG).show()
        }else if (!isValidEmail(email)){
            Toast.makeText(this, "Email is not valid!", Toast.LENGTH_LONG).show()
        }else{

            firebaseAuth = FirebaseAuth.getInstance()

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if(it.isSuccessful){
                    if(firebaseAuth.currentUser?.toString() != null){
                        val intent = Intent(this,MainActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this, "Cannot find User!", Toast.LENGTH_LONG).show()
                    }
                }else if(!it.isSuccessful){
                    Toast.makeText(this, "Email or Password does not match!", Toast.LENGTH_LONG).show()
                }
            }.addOnCanceledListener {
                Toast.makeText(this, "Sign in failed!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }
}