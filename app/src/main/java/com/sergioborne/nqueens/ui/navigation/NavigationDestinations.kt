package com.sergioborne.nqueens.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable


sealed interface MainDestinations : NavKey {
    @Serializable
    data object MainScreen : MainDestinations

    @Serializable
    data class ChessboardScreen(val boardSize: Int) : MainDestinations
}
