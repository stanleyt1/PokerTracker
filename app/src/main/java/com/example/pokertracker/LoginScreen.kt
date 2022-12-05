package com.example.pokertracker

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.pokertracker.databinding.ActivityLoginScreenBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.OnCompleteListener

class LoginScreen : AppCompatActivity() {

    private lateinit var binding: ActivityLoginScreenBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*Use for Debugging HomeScreen
        val intent = Intent(this, HomeScreen::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //clearing background activity
        //logging in user
        startActivity(intent)
        */

        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()


        binding.SignUp.setOnClickListener {
            val intent = Intent(this, SignUpScreen::class.java)
            //logging in user
            startActivity(intent)
        }
        binding.LogInButton.setOnClickListener {
            //storing entered information
            val email = binding.Email.text.toString()
            val password = binding.Password.text.toString()
            if (email.isNotEmpty()) {
                if (password.isNotEmpty()) {
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener() {
                            if (it.isSuccessful) {
                                Log.e("TAG", "success",it.exception)
                                Toast.makeText(
                                    this,
                                    "Successfully Registered",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this, HomeScreen::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //clearing background activity
                                //logging in user
                                startActivity(intent)
                                finish()


                            } else {
                                Toast.makeText(
                                    this,
                                    it.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(
                        this,
                        "Please enter a password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this,
                    "Please enter your email",
                    Toast.LENGTH_SHORT
                ).show()
            }


        }
    }
}