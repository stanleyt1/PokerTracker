package com.example.pokertracker

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pokertracker.databinding.ActivityLoginScreenBinding
import com.google.firebase.auth.FirebaseAuth

class LoginScreen : AppCompatActivity() {

    private lateinit var binding: ActivityLoginScreenBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                                val user = FirebaseAuth.getInstance().currentUser
                                val emailVerified = user!!.isEmailVerified
                                if(emailVerified) {
                                    Toast.makeText(
                                        this,
                                        "Successfully Logged In",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(this, MainActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //clearing background activity
                                    //logging in user
                                    startActivity(intent)
                                    finish()
                                }else{
                                    Toast.makeText(
                                        this,
                                        "Email not verified",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    FirebaseAuth.getInstance().signOut();


                                }


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