package com.example.pokertracker.models

import java.util.*

data class Session(
    val id: String,
    val location: String,
    val date: Date,
    val gameType: String,
    val buyInAmount: Number,
    val payoutAmount: Number,
    val smallBlind: Number,
    val bigBlind: Number,
    //    val name: String,
)
