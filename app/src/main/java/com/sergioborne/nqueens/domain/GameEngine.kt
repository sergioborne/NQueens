package com.sergioborne.nqueens.domain

import javax.inject.Inject
import kotlin.math.abs

class GameEngine @Inject constructor() {

    private val occupiedCells = mutableListOf<CellData>()
    private var boardSize: Int = -1

    fun initGameEngine(size: Int) {
        boardSize = size
    }

    fun changePosition(rowPosition: Int, columnPosition: Int): List<CellData> {
        if (boardSize < 0) {
            throw IllegalArgumentException("Board size not initialized")
        }

        if (occupiedCells.removeIf { it.row == rowPosition && it.column == columnPosition }.not()) {
            occupiedCells.add(
                CellData(
                    row = rowPosition,
                    column = columnPosition,
                    isQueen = true,
                    isAttacked = false,
                )
            )
        }

        return occupiedCells.calculateAttackingPositions()
    }

    private fun List<CellData>.calculateAttackingPositions(): List<CellData> =
        map { cell ->
            val cellRow = cell.row
            val cellColumn = cell.column

            val isAttacked =
                if (cell.isQueen) {
                    any { (queenRow, queenColumn) ->
                        (cellRow != queenRow || cellColumn != queenColumn)
                                && (cellRow == queenRow || cellColumn == queenColumn || abs(
                            cellRow - queenRow
                        ) == abs(cellColumn - queenColumn))
                    }
                } else {
                    false
                }
            cell.copy(isAttacked = isAttacked)
        }

}