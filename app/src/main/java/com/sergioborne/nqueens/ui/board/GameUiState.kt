package com.sergioborne.nqueens.ui.board

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.abs

@Stable
data class GameUiState(
    val boardState: BoardUiState,
    val remainingQueens: Int,
    val elapsedTime: Long,
    val isVictory: Boolean,
)

@Stable
data class BoardUiState(
    val size: Int,
    val occupiedCells: ImmutableList<CellUi>,
) {

    companion object {
        fun empty(size: Int) = BoardUiState(
            size = size,
            occupiedCells = persistentListOf(),
        )
    }
}

@Stable
data class CellUi(
    val row: Int,
    val column: Int,
    val isQueen: Boolean,
    val isAttacked: Boolean,
)
