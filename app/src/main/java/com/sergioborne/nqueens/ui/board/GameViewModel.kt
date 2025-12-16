package com.sergioborne.nqueens.ui.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergioborne.nqueens.domain.CellData
import com.sergioborne.nqueens.domain.GameEngine
import com.sergioborne.nqueens.domain.Score
import com.sergioborne.nqueens.repository.LeaderboardRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
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
    private val leaderboardRepository: LeaderboardRepository,
    private val gameEngine: GameEngine,
) : ViewModel() {

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
        data object ShowError : Event()
    }

    init {
        gameEngine.initGameEngine(boardSize)
    }

    fun onCellClicked(rowPosition: Int, columnPosition: Int, timeElapsed: Long) {
        val cells = tryChangePosition(rowPosition = rowPosition, columnPosition = columnPosition)
        val remainingQueens = boardSize - cells.size
        val isVictory = remainingQueens == 0 && cells.none { it.isAttacked }

        _uiState.update {
            GameUiState(
                boardState = BoardUiState(
                    size = boardSize,
                    occupiedCells = cells.toCellsUi(),
                ),
                remainingQueens = remainingQueens,
                isVictory = isVictory,
                elapsedTime = timeElapsed,
            )
        }
    }

    fun onClearButtonClicked() {
        gameEngine.clearCells()
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
                    time = uiState.value.elapsedTime,
                    boardSize = boardSize,
                    date = Date(),
                )
            )
            _events.trySend(Event.VictorySaved)
        }
    }

    private fun tryChangePosition(rowPosition: Int, columnPosition: Int): List<CellData> = try {
        gameEngine.changePosition(rowPosition, columnPosition)
    } catch (illegalArgumentException: IllegalArgumentException) {
        illegalArgumentException.printStackTrace()
        viewModelScope.launch {
            _events.send(Event.ShowError)
        }
        emptyList()
    }

    private fun List<CellData>.toCellsUi() = map {
        CellUi(
            row = it.row,
            column = it.column,
            isQueen = it.isOccupied,
            isAttacked = it.isAttacked,
        )
    }.toImmutableList()

    @AssistedFactory
    interface Factory {
        fun create(boardSize: Int): GameViewModel
    }
}
