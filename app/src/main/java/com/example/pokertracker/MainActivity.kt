package com.example.pokertracker

import androidx.fragment.app.FragmentActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.pokertracker.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : FragmentActivity() {

    private lateinit var bottomNavigationView : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFragment(StatsFragment())


        // handles navigation
        bottomNavigationView = findViewById(R.id.bottomNavigationView) as BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.stats -> {
                    loadFragment(StatsFragment())
                }
                R.id.session -> {
                    loadFragment(SessionFragment())
                }
                R.id.history -> {
                    loadFragment(HistoryFragment()) }
                else -> {
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    }
