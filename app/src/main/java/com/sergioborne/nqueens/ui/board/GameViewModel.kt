package com.sergioborne.nqueens.ui.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergioborne.nqueens.domain.Score
import com.sergioborne.nqueens.repository.LeaderboardRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

@HiltViewModel(assistedFactory = GameViewModel.Factory::class)
class GameViewModel @AssistedInject constructor(
    @Assisted val boardSize: Int,
    val leaderboardRepository: LeaderboardRepository,
) : ViewModel() {

    private var timerJob: Job? = null
    private var elapsedTimeInMillis = 0L

    private val _uiState = MutableStateFlow(
        GameUiState(
            boardState = BoardUiState.empty(boardSize),
            remainingQueens = boardSize,
            isVictory = false,
            elapsedTime = 0L,
        )
    )
    val uiState: StateFlow<GameUiState> = _uiState

    private val _events = Channel<Event>()
    val events: Flow<Event>
        get() = _events.receiveAsFlow()

    sealed class Event {
        data object VictorySaved : Event()
    }

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

    fun onVictorySave(name: String) {
        viewModelScope.launch {
            leaderboardRepository.insertScore(
                Score(
                    name = name,
                    time = elapsedTimeInMillis,
                    boardSize = boardSize,
                    date = Date(),
                )
            )
            _events.trySend(Event.VictorySaved)
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                _uiState.update { it.copy(elapsedTime = elapsedTimeInMillis) }
                delay(TIMER_DELAY_MS)
                elapsedTimeInMillis += TIMER_DELAY_MS
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
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
