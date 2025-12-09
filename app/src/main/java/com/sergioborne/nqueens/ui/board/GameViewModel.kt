package com.sergioborne.nqueens.ui.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

@HiltViewModel(assistedFactory = GameViewModel.Factory::class)
class GameViewModel @AssistedInject constructor(
    @Assisted val boardSize: Int,
) : ViewModel() {

    private var timerJob: Job? = null
    private var elapsedTimeInMillis = 0L

    private val _uiState = MutableStateFlow(
        GameUiState(
            boardState = BoardUiState.empty(boardSize),
            remainingQueens = boardSize,
            isVictory = false,
            elapsedTime = "0:00.00",
        )
    )
    val uiState: StateFlow<GameUiState> = _uiState

    init {
        startTimer()
    }

    fun onCellClicked(rowPosition: Int, columnPosition: Int) {
        val newBoardState = _uiState.value.boardState.changePosition(rowPosition, columnPosition)
        val remainingQueens = boardSize - newBoardState.cells.count { it.isQueen }
        val isVictory = remainingQueens == 0 && newBoardState.cells.none { it.isAttacked }

        if (isVictory) {
            stopTimer()
        }

        _uiState.update {
            GameUiState(
                boardState = newBoardState,
                remainingQueens = remainingQueens,
                isVictory = isVictory,
                elapsedTime = uiState.value.elapsedTime,
            )
        }
    }

    fun onClearButtonClicked() {
        _uiState.update {
            GameUiState(
                boardState = BoardUiState.empty(boardSize),
                remainingQueens = boardSize,
                isVictory = false,
                elapsedTime = uiState.value.elapsedTime,
            )
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                _uiState.update { it.copy(elapsedTime = elapsedTimeInMillis.formatPreciseTime()) }
                delay(TIMER_DELAY_MS)
                elapsedTimeInMillis += TIMER_DELAY_MS
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
    }

    fun Long.formatPreciseTime(): String {
        val totalSeconds = this / 1000
        val cents = (this % 1000) / 10
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format(Locale.getDefault(), "%02d:%02d.%2d", minutes, seconds, cents)
    }

    companion object {
        private const val TIMER_DELAY_MS = 10L
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
    }

    @AssistedFactory
    interface Factory {
        fun create(boardSize: Int): GameViewModel
    }
}
