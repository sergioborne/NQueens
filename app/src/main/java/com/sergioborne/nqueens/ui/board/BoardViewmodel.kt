package com.sergioborne.nqueens.ui.board

import androidx.lifecycle.ViewModel
import com.sergioborne.nqueens.ui.BoardUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class BoardViewmodel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(BoardUiState.empty(8))
    val uiState: StateFlow<BoardUiState> = _uiState

    fun onCellClicked(rowPosition: Int, columnPosition: Int) {
        _uiState.value = _uiState.value.changePosition(rowPosition, columnPosition)
    }
}
