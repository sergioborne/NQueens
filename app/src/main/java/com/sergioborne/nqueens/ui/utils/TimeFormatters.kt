package com.sergioborne.nqueens.ui.utils

import java.util.Locale

fun Long.formatPreciseTime(): String {
    val totalSeconds = this / 1000
    val cents = (this % 1000) / 10
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.getDefault(), "%02d:%02d.%02d", minutes, seconds, cents)
}