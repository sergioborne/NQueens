package com.sergioborne.nqueens.domain

data class CellData(
    val row: Int,
    val column: Int,
    val isQueen: Boolean,
    val isAttacked: Boolean,
)