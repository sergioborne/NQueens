package com.sergioborne.nqueens.domain

import java.util.Date

data class Score(
    val name: String,
    val time: Long,
    val boardSize: Int,
    val date: Date,
)