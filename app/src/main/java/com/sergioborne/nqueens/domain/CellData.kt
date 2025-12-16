package com.sergioborne.nqueens.domain

data class CellData(
    val row: Int,
    val column: Int,
    val isOccupied: Boolean,
    val isAttacked: Boolean,
)