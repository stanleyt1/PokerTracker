package com.example.pokertracker

data class Session(
    val location: String? = "",
    val date: String? = "",
    val game_type: String? = "",
    val buy_in: Double? = 0.0,
    val pay_out: Double? = 0.0,
    val hands_played: Int? = 0,
    val hands_won: Int? = 0,
)