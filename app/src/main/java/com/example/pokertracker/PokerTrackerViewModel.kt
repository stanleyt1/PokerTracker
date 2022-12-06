package com.example.pokertracker

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase



class PokerTrackerViewModel: ViewModel() {

    private var database: DatabaseReference = Firebase.database.reference


    fun getSessions(uid: String): ArrayList<Session> {

        var sessions: ArrayList<Session> = ArrayList()


        database.child(uid).get()
            .addOnSuccessListener {
                sessions = it.getValue<ArrayList<Session>>()!!
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }

        return sessions
    }
}