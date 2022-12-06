package com.example.pokertracker

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.floor

class SessionFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var addSessionButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth  = Firebase.auth
        database =  Firebase.database.reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_session, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        addSessionButton = requireView().findViewById(R.id.add_session) as Button

        addSessionButton.setOnClickListener {

            val location = (requireView().findViewById(R.id.location) as EditText).text.toString()


            val date = (requireView().findViewById(R.id.date) as EditText).text.toString()
            if (!isDate(date)) {
                Toast.makeText(
                    activity,
                    "Invalid Date",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val gameType = if (requireView().findViewById<RadioButton>(R.id.fivedraw).isChecked) {
                "Five Card Draw"
            } else if (requireView().findViewById<RadioButton>(R.id.holdem).isChecked) {
                "Five Card Draw"
            } else {
                "Omaha"
            }.toString()

            val buyIn = (requireView().findViewById<EditText>(R.id.buyIn)).text.toString()
            val payOut = (requireView().findViewById<EditText>(R.id.payout)).text.toString()
            val handsPlayed = (requireView().findViewById<EditText>(R.id.hands_played)).text.toString()
            val handsWon = (requireView().findViewById<EditText>(R.id.hands_won)).text.toString()

            if (location == "" || buyIn == "" || payOut == "" || handsPlayed == "" || handsWon == "") {
                Toast.makeText(
                    activity,
                    "Fields can not be left blank. ",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (handsWon.toInt() > handsPlayed.toInt()) {
                Toast.makeText(
                    activity,
                    "Hands won can not be greater than hands played ",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (handsPlayed.toInt() == 0) {
                Toast.makeText(
                    activity,
                    "Hands played can not be zero. ",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (floor(buyIn.toDouble() * 100) != (buyIn.toDouble() * 100)) {
                Toast.makeText(
                    activity,
                    "Buy in must be a valid monetary amount. ",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (floor(payOut.toDouble() * 100) != (payOut.toDouble() * 100)) {
                Toast.makeText(
                    activity,
                    "Payout must be a valid monetary amount. ",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }



            // add session to database
            val key = database.child(auth.currentUser!!.uid).push().key


            Log.i("elo", "$key $location $gameType $date $buyIn $payOut $handsPlayed $handsWon")

            if (key != null) {
                val ref = database.child(auth.currentUser!!.uid).child(key)

                val listener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        // clear fields
                        (requireView().findViewById(R.id.location) as EditText).setText("")
                        (requireView().findViewById(R.id.date) as EditText).setText("")
                        (requireView().findViewById<EditText>(R.id.buyIn)).setText("")
                        (requireView().findViewById<EditText>(R.id.payout)).setText("")
                        (requireView().findViewById<EditText>(R.id.hands_played)).setText("")
                        (requireView().findViewById<EditText>(R.id.hands_won)).setText("")


                        Toast.makeText(
                            activity,
                            "Success!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Toast.makeText(
                            activity,
                            "Something went wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                ref.addValueEventListener(listener)

                ref.setValue(Session (location, date, gameType, buyIn.toDouble(), payOut.toDouble(), handsPlayed.toInt(), handsWon.toInt()))
            }

        }
    }

    fun isDate(date: String): Boolean {
//        val formatter = SimpleDateFormat("dd/MM/yyyy")

        return try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            true
        } catch (e: Exception) {
            false
        }
    }

}