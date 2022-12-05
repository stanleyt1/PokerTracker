package com.example.pokertracker


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pokertracker.databinding.ActivitySignUpScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpScreenBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpScreenBinding.inflate(layoutInflater)


        setContentView(binding.root)


        binding.LogIn.setOnClickListener {
            val intent = Intent(this, LoginScreen::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //clearing background activity
            //logging in user
            startActivity(intent)
            finish()

        }

        firebaseAuth = FirebaseAuth.getInstance()
        binding.RegisterButton.setOnClickListener {
            //storing entered information
            val email = binding.Email.text.toString()
            val fullName = binding.name.text.toString()
            val password = binding.Password.text.toString()
            val confirmPassword = binding.ConfirmPassword.text.toString()
            if (fullName.isNotEmpty()) {
                if (email.isNotEmpty()) {
                    if (password.isNotEmpty()) {
                        if (confirmPassword.isNotEmpty()) {
                            if (password == confirmPassword) {
                                firebaseAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            val firebaseUser: FirebaseUser = it.result!!.user!!

                                            firebaseUser.sendEmailVerification()
                                            Toast.makeText(
                                                this,
                                                "Successfully Registered, Check Verification Email",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            val intent = Intent(this, LoginScreen::class.java)
                                            intent.flags =
                                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //clearing background activity
                                            //logging in user
                                            intent.putExtra("Email", email)
                                            intent.putExtra("Password", password)
                                            intent.putExtra("Name", fullName)
                                            startActivity(intent)
                                            finish() //finish register activity
                                        } else {
                                            Toast.makeText(
                                                this,
                                                it.exception!!.message.toString(),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            Log.e("TAG", "failed",it.exception)
                                        }
                                    }
                            } else {
                                Toast.makeText(
                                    this,
                                    "Passwords do not match",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this,
                                "Please confirm your password",
                                Toast.LENGTH_SHORT
                            ).show()
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

            } else {
                Toast.makeText(
                    this,
                    "Please enter your full name",
                    Toast.LENGTH_SHORT
                ).show()
            }


        }
    }


}


