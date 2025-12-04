package com.sergioborne.nqueens.ui.board

import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel(assistedFactory = GameViewModel.Factory::class)
class GameViewModel @AssistedInject constructor(
    @Assisted val boardSize: Int,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        GameUiState(
            boardState = BoardUiState.empty(boardSize),
            remainingQueens = boardSize,
            isVictory = false,
        )
    )
    val uiState: StateFlow<GameUiState> = _uiState

    fun onCellClicked(rowPosition: Int, columnPosition: Int) {
        val newBoardState = _uiState.value.boardState.changePosition(rowPosition, columnPosition)
        val remainingQueens = boardSize - newBoardState.cells.flatten().count { it.isQueen }
        val isVictory = remainingQueens == 0 && newBoardState.cells.flatten().none { it.isAttacked }

        _uiState.value = GameUiState(
            boardState = newBoardState,
            remainingQueens = remainingQueens,
            isVictory = isVictory
        )
    }

    @AssistedFactory
    interface Factory {
        fun create(boardSize: Int): GameViewModel
    }
}
