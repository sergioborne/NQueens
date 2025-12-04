package com.sergioborne.nqueens.ui.board

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onVictoryAnimationFinished: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        GameContent(
            boardUiState = state.boardState,
            modifier = Modifier.padding(innerPadding),
            onCellClicked = viewModel::onCellClicked
        )
        if (state.isVictory) {
            FireworksAnimation(onAnimationFinished = onVictoryAnimationFinished)
        }
    }
}

@Composable
private fun GameContent(
    boardUiState: BoardUiState,
    modifier: Modifier = Modifier,
    onCellClicked: (Int, Int) -> Unit,
) {
    Chessboard(
        boardUiState = boardUiState,
        modifier = modifier,
        onCellClicked = onCellClicked
    )

}
