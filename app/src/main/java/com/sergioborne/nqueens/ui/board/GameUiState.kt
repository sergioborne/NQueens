package com.sergioborne.nqueens.ui.board

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.abs

@Stable
data class GameUiState(
    val boardState: BoardUiState,
    val remainingQueens: Int,
    val elapsedTime: String,
    val isVictory: Boolean,
)

@Stable
data class BoardUiState(
    val size: Int,
    val cells: ImmutableList<CellUi>,
) {

    fun changePosition(rowPosition: Int, columnPosition: Int): BoardUiState {
        val targetIndex = rowPosition * size + columnPosition

        val newCellsWithQueenToggled = cells.toMutableList()
        val oldCell = cells[targetIndex]
        newCellsWithQueenToggled[targetIndex] = oldCell.copy(isQueen = !oldCell.isQueen)

        val queenPositions = mutableListOf<Pair<Int, Int>>()
        newCellsWithQueenToggled.forEachIndexed { index, cell ->
            if (cell.isQueen) {
                val r = index / size
                val c = index % size
                queenPositions.add(r to c)
            }
        }

        val finalCells = newCellsWithQueenToggled.mapIndexed { index, cell ->
            val r = index / size
            val c = index % size

            val isAttacked =
                if (cell.isQueen) { // The cell is a queen, check if another queen attacks it
                    queenPositions.any { (qr, qc) ->
                        (r != qr || c != qc) && (r == qr || c == qc || abs(r - qr) == abs(c - qc))
                    }
                } else {
                    false
                }
            cell.copy(isAttacked = isAttacked)
        }.toImmutableList()

        return this.copy(cells = finalCells)
    }


    companion object {
        fun empty(size: Int) = BoardUiState(
            size = size,
            cells = List(size * size) {
                CellUi(
                    isQueen = false,
                    isAttacked = false,
                )
            }.toImmutableList()
        )
    }
}

@Stable
data class CellUi(
    val isQueen: Boolean,
    val isAttacked: Boolean,
)
