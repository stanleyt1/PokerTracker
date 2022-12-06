package com.example.pokertracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var loginButton: TextView
    private lateinit var signUpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        // Initialize Firebase Auth
        auth = Firebase.auth

        // try to sign up a new user
        signUpButton = findViewById(R.id.singupButton)
        signUpButton.setOnClickListener {

            val email = findViewById<EditText>(R.id.email).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()
            val confirmPassword = findViewById<EditText>(R.id.confirm_password).text.toString()

            if (email == "" || password == "" || confirmPassword == "") {
                Toast.makeText(baseContext, "Fields can not be left blank.",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(baseContext, "Passwords do not match.",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // try to create a new user
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {task ->
                    if (task.isSuccessful) {

                        // navigate to home screen
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, "Unable to create account.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }


        // navigate to login screen if user has an account
        loginButton = findViewById(R.id.login)
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}