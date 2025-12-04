package com.sergioborne.nqueens.ui

import androidx.compose.runtime.Stable

@Stable
data class BoardUiState(
    val size: Int,
    val cells: List<List<CellUi>>
) {

    fun changePosition(rowPosition: Int, columnPosition: Int): BoardUiState =
        this.copy(
            size = this.size,
            cells = this.cells.mapIndexed { r, row ->
                row.mapIndexed { c, cell ->
                    if (r == rowPosition && c == columnPosition) {
                        cell.copy(isQueen = cell.isQueen.not())
                    } else {
                        cell
                    }
                }
            }
        )

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