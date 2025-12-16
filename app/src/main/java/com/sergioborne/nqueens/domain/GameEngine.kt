package com.sergioborne.nqueens.domain

import javax.inject.Inject

class GameEngine @Inject constructor() {

    private var occupiedCells = listOf<CellData>()
    private var boardSize: Int = -1

    fun initGameEngine(size: Int) {
        boardSize = size
    }

    fun changePosition(rowPosition: Int, columnPosition: Int): List<CellData> {
        if (boardSize < 0) {
            throw IllegalArgumentException("Board size not initialized")
        }

        occupiedCells =
            if (occupiedCells.any { it.row == rowPosition && it.column == columnPosition }) {
                occupiedCells.filterNot { it.row == rowPosition && it.column == columnPosition }
            } else {
                occupiedCells + CellData(
                    row = rowPosition,
                    column = columnPosition,
                    isOccupied = true,
                    isAttacked = false,
                )
            }.calculateAttackingPositions()

        return occupiedCells
    }

    fun clearCells() {
        occupiedCells = emptyList()
    }

    private fun List<CellData>.calculateAttackingPositions(): List<CellData> {
        val rowCounts = this.groupingBy { it.row }.eachCount()
        val colCounts = this.groupingBy { it.column }.eachCount()
        val sumDiagonalCounts = this.groupingBy { it.row + it.column }.eachCount()
        val diffDiagonalCounts = this.groupingBy { it.row - it.column }.eachCount()

        return this.map { queen ->
            val isAttacked = ((rowCounts[queen.row] ?: 0) > 1) ||
                    ((colCounts[queen.column] ?: 0) > 1) ||
                    ((sumDiagonalCounts[queen.row + queen.column] ?: 0) > 1) ||
                    ((diffDiagonalCounts[queen.row - queen.column] ?: 0) > 1)

            queen.copy(isAttacked = isAttacked)
        }
    }

}