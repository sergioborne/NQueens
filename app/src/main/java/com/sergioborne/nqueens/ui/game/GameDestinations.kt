package com.sergioborne.nqueens.ui.game

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable


sealed interface GameDestinations : NavKey {

    @Serializable
    data object BoardSizeScreen : GameDestinations

    @Serializable
    data class ChessboardScreen(val boardSize: Int) : GameDestinations
}
