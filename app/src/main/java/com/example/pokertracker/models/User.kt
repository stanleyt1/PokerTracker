package com.example.pokertracker.models

data class User(
    val uid: String,
    val firstName: String,
    val email: String,
    val sessions: List<Session>
)
