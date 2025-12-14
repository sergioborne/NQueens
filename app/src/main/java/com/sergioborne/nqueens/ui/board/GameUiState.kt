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

    fun changePosition(rowPosition: Int, columnPosition: Int): BoardUiState {
        val newList: List<CellUi> =
            if (occupiedCells.any { it.row == rowPosition && it.column == columnPosition }) {
                occupiedCells.filterNot { it.row == rowPosition && it.column == columnPosition }
            } else {
                occupiedCells + CellUi(rowPosition, columnPosition, true, false)
            }

        val finalCells = newList.map { cell ->
            val r = cell.row
            val c = cell.column

            val isAttacked =
                if (cell.isQueen) {
                    newList.any { (qr, qc) ->
                        (r != qr || c != qc) && (r == qr || c == qc || abs(r - qr) == abs(c - qc))
                    }
                } else {
                    false
                }
            cell.copy(isAttacked = isAttacked)
        }.toImmutableList()

        return this.copy(occupiedCells = finalCells)
    }


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
