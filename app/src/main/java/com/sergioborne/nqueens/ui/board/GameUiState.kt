package com.sergioborne.nqueens.ui.board

import androidx.compose.runtime.Stable
import kotlin.math.abs

@Stable
data class GameUiState(
    val boardState: BoardUiState,
    val remainingQueens: Int,
    val isVictory: Boolean,
)

@Stable
data class BoardUiState(
    val size: Int,
    val cells: List<List<CellUi>>
) {

    fun changePosition(rowPosition: Int, columnPosition: Int): BoardUiState {
        val newCellsWithQueenToggled = cells.mapIndexed { r, row ->
            row.mapIndexed { c, cell ->
                if (r == rowPosition && c == columnPosition) {
                    cell.copy(isQueen = !cell.isQueen)
                } else {
                    cell
                }
            }
        }

        val queenPositions = mutableListOf<Pair<Int, Int>>()
        newCellsWithQueenToggled.forEachIndexed { r, row ->
            row.forEachIndexed { c, cell ->
                if (cell.isQueen) {
                    queenPositions.add(r to c)
                }
            }
        }

        val finalCells = newCellsWithQueenToggled.mapIndexed { r, row ->
            row.mapIndexed { c, cell ->
                if (!cell.isQueen) {
                    cell.copy(isAttacked = false)
                } else {
                    val isAttacked = queenPositions.any { (qr, qc) ->
                        (r != qr || c != qc) && (r == qr || c == qc || abs(r - qr) == abs(c - qc))
                    }
                    cell.copy(isAttacked = isAttacked)
                }
            }
        }

        return this.copy(cells = finalCells)
    }


    companion object {
        fun empty(size: Int) = BoardUiState(
            size = size,
            cells = List(size) {
                List(size) {
                    CellUi(
                        isQueen = false,
                        isAttacked = false,
                    )
                }
            }
        )
    }
}

data class CellUi(
    val isQueen: Boolean,
    val isAttacked: Boolean,
)
