package com.sergioborne.nqueens.ui.board

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sergioborne.nqueens.ui.theme.NQueensTheme

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onVictoryAnimationFinished: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box {
            GameContent(
                gameUiState = state,
                modifier = Modifier
                    .padding(innerPadding),
                onCellClicked = viewModel::onCellClicked,
                onClearButtonClick = viewModel::onClearButtonClicked,
            )
            if (state.isVictory) {
                ConfettiAnimation(onAnimationFinished = onVictoryAnimationFinished)
            }
        }
    }
}

@Composable
private fun GameContent(
    gameUiState: GameUiState,
    modifier: Modifier = Modifier,
    onCellClicked: (Int, Int) -> Unit,
    onClearButtonClick: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    if (isPortrait) {
        PortraitGameLayout(
            gameUiState = gameUiState,
            modifier = modifier,
            onCellClicked = onCellClicked,
            onClearButtonClick = onClearButtonClick,
        )
    } else {
        LandscapeGameLayout(
            gameUiState = gameUiState,
            modifier = modifier,
            onCellClicked = onCellClicked,
            onClearButtonClick = onClearButtonClick,
        )
    }
}

@Composable
private fun PortraitGameLayout(
    gameUiState: GameUiState,
    modifier: Modifier = Modifier,
    onCellClicked: (Int, Int) -> Unit,
    onClearButtonClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GameStatus(
            remainingQueens = gameUiState.remainingQueens,
            elapsedTime = gameUiState.elapsedTime,
            isPortrait = true,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Chessboard(
                boardUiState = gameUiState.boardState,
                modifier = modifier,
                onCellClicked = onCellClicked,
            )
            Button(onClick = onClearButtonClick) {
                Text(text = "Clear")
            }
        }
    }
}

@Composable
private fun LandscapeGameLayout(
    gameUiState: GameUiState,
    modifier: Modifier = Modifier,
    onCellClicked: (Int, Int) -> Unit,
    onClearButtonClick: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        GameStatus(
            remainingQueens = gameUiState.remainingQueens,
            elapsedTime = gameUiState.elapsedTime,
            isPortrait = false,
        )
        Chessboard(
            boardUiState = gameUiState.boardState,
            modifier = Modifier.fillMaxHeight(),
            onCellClicked = onCellClicked,
        )
        Button(onClick = onClearButtonClick) {
            Text(text = "Clear")
        }
    }
}

@Composable
private fun GameStatus(
    remainingQueens: Int,
    elapsedTime: String,
    isPortrait: Boolean,
    modifier: Modifier = Modifier
) {
    val alignment = if (isPortrait) Alignment.CenterHorizontally else Alignment.Start
    Column(modifier = modifier.padding(16.dp), horizontalAlignment = alignment) {
        Text(
            text = "Queens to place: $remainingQueens",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        TimerText(elapsedTime = elapsedTime)
    }
}

@Composable
private fun TimerText(elapsedTime: String, modifier: Modifier = Modifier) {
    Text(
        text = elapsedTime,
        modifier = modifier,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold
    )
}


@Preview
@Composable
fun GameContentPreview() {
    NQueensTheme {
        GameContent(
            gameUiState = GameUiState(
                boardState = BoardUiState.empty(8),
                remainingQueens = 8,
                isVictory = false,
                elapsedTime = "0:00.00",
            ),
            onCellClicked = { _, _ -> },
            onClearButtonClick = {},
        )
    }
}