package com.sergioborne.nqueens.ui.navigation

import androidx.annotation.DrawableRes
import androidx.compose.runtime.saveable.Saver
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import com.sergioborne.nqueens.R


sealed interface MainDestinations : NavKey {
    val label: String

    @get:DrawableRes
    val iconRes: Int

    @Serializable
    data object GameScreen : MainDestinations {
        override val label: String = "Game"
        override val iconRes: Int = R.drawable.ic_chessboard
    }

    @Serializable
    data object LeaderboardScreen : MainDestinations {
        override val label: String = "Leaderboard"
        override val iconRes: Int = R.drawable.ic_leaderboard
    }

    companion object {
        val items = listOf(GameScreen, LeaderboardScreen)

        val stateSaver = Saver<MainDestinations, String>(
            save = { it::class.qualifiedName },
            restore = { qualifiedClass ->
                items.firstOrNull { it::class.qualifiedName == qualifiedClass }
                    ?: GameScreen
            }
        )
    }
}
