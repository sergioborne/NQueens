package com.sergioborne.nqueens.ui.leaderboard

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergioborne.nqueens.repository.LeaderboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val leaderboardRepository: LeaderboardRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<LeaderboardUiState>(LeaderboardUiState.Loading)
    val uiState: StateFlow<LeaderboardUiState> = _uiState

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            leaderboardRepository.getAllScores()
                .catch { it.printStackTrace() }
                .collect { scores ->
                    _uiState.value = if (scores.isEmpty()) {
                        LeaderboardUiState.Empty
                    } else {
                        LeaderboardUiState.Content(
                            leaderboardEntries = scores
                                .sortedWith(
                                    compareBy(
                                        { it.boardSize },
                                        { it.time }
                                    )
                                ).map { score ->
                                    LeaderboardEntry(
                                        name = score.name,
                                        boardSize = score.boardSize,
                                        timeTakenMillis = score.time,
                                    )
                                }.toImmutableList()
                        )
                    }
                }
        }
    }

    fun clearLeaderboardClick() {
        viewModelScope.launch {
            leaderboardRepository.clear()
        }
    }

}

@Stable
sealed interface LeaderboardUiState {
    data class Content(
        val leaderboardEntries: ImmutableList<LeaderboardEntry> = persistentListOf(),
    ) : LeaderboardUiState

    data object Loading : LeaderboardUiState
    data object Empty : LeaderboardUiState
}

@Stable
data class LeaderboardEntry(
    val name: String,
    val boardSize: Int,
    val timeTakenMillis: Long
)
