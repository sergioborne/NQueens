package com.sergioborne.nqueens.ui.board

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sergioborne.nqueens.ui.theme.NQueensTheme
import com.sergioborne.nqueens.ui.utils.ThemePreviewsWithBackground
import com.sergioborne.nqueens.ui.utils.formatPreciseTime
import com.sergioborne.nqueens.ui.victory.VictoryScreen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.isActive

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onVictorySaved: () -> Unit,
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.events, onVictorySaved) {
        viewModel.events.catch { it.printStackTrace() }
            .collect { event ->
                when (event) {
                    is GameViewModel.Event.VictorySaved -> {
                        onVictorySaved()
                    }

                    is GameViewModel.Event.ShowError -> {
                        Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Box {
            GameContent(
                size = state.boardState.size,
                remainingQueens = state.remainingQueens,
                isVictory = state.isVictory,
                occupiedCells = state.boardState.occupiedCells,
                modifier = Modifier
                    .background(NQueensTheme.colors.background)
                    .padding(innerPadding),
                onCellClicked = viewModel::onCellClicked,
                onClearButtonClick = viewModel::onClearButtonClicked,
            )
            if (state.isVictory) {
                VictoryScreen(
                    onVictorySaved = viewModel::onVictorySave,
                )
            }
        }
    }
}

@Composable
private fun GameContent(
    size: Int,
    remainingQueens: Int,
    isVictory: Boolean,
    occupiedCells: ImmutableList<CellUi>,
    modifier: Modifier = Modifier,
    onCellClicked: (Int, Int, Long) -> Unit,
    onClearButtonClick: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val time = rememberSaveable { mutableLongStateOf(0L) }

    if (isPortrait) {
        PortraitGameLayout(
            size = size,
            remainingQueens = remainingQueens,
            elapsedTime = time,
            occupiedCells = occupiedCells,
            isVictory = isVictory,
            modifier = modifier,
            onCellClicked = { row, column ->
                onCellClicked(row, column, time.longValue)
            },
            onClearButtonClick = onClearButtonClick,
        )
    } else {
        LandscapeGameLayout(
            size = size,
            remainingQueens = remainingQueens,
            elapsedTime = time,
            occupiedCells = occupiedCells,
            isVictory = isVictory,
            modifier = modifier,
            onCellClicked = { row, column ->
                onCellClicked(row, column, time.longValue)
            },
            onClearButtonClick = onClearButtonClick,
        )
    }
}

@Composable
private fun PortraitGameLayout(
    size: Int,
    remainingQueens: Int,
    occupiedCells: ImmutableList<CellUi>,
    elapsedTime: MutableState<Long>,
    isVictory: Boolean,
    modifier: Modifier = Modifier,
    onCellClicked: (Int, Int) -> Unit,
    onClearButtonClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NQueensTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GameStatus(
            remainingQueens = remainingQueens,
            elapsedTime = elapsedTime,
            isPortrait = true,
            isVictory = isVictory,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Chessboard(
                size = size,
                occupiedCells = occupiedCells,
                modifier = Modifier.padding(32.dp),
                onCellClicked = onCellClicked,
            )
            ClearBoardButton(onClearButtonClick)
        }
    }
}

@Composable
private fun LandscapeGameLayout(
    size: Int,
    remainingQueens: Int,
    elapsedTime: MutableState<Long>,
    isVictory: Boolean,
    occupiedCells: ImmutableList<CellUi>,
    modifier: Modifier = Modifier,
    onCellClicked: (Int, Int) -> Unit,
    onClearButtonClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .background(NQueensTheme.colors.background),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        GameStatus(
            remainingQueens = remainingQueens,
            elapsedTime = elapsedTime,
            isPortrait = false,
            isVictory = isVictory,
        )
        Chessboard(
            size = size,
            occupiedCells = occupiedCells,
            modifier = Modifier.fillMaxHeight(),
            onCellClicked = onCellClicked,
        )
        ClearBoardButton(onClearButtonClick)
    }
}

@Composable
private fun GameStatus(
    remainingQueens: Int,
    elapsedTime: MutableState<Long>,
    isVictory: Boolean,
    isPortrait: Boolean,
    modifier: Modifier = Modifier
) {
    val alignment = if (isPortrait) Alignment.CenterHorizontally else Alignment.Start
    Column(modifier = modifier.padding(16.dp), horizontalAlignment = alignment) {
        Text(
            text = "Queens to place: $remainingQueens",
            style = MaterialTheme.typography.bodyLarge,
            color = NQueensTheme.colors.textPrimary,
        )
        Spacer(modifier = Modifier.height(8.dp))
        TimerText(
            elapsedTime = elapsedTime,
            isVictory = isVictory,
        )
    }
}

@Composable
private fun TimerText(
    elapsedTime: MutableState<Long>,
    isVictory: Boolean,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(isVictory) {
        while (isActive && !isVictory) {
            delay(10)
            elapsedTime.value += 10
        }
    }
    Text(
        text = elapsedTime.value.formatPreciseTime(),
        modifier = modifier,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = NQueensTheme.colors.textPrimary
    )
}

@Composable
private fun ClearBoardButton(onClearButtonClick: () -> Unit) {
    Button(
        onClick = onClearButtonClick,
        modifier = Modifier.testTag("clear-button"),
    ) {
        Text(text = "Clear")
    }
}

@ThemePreviewsWithBackground
@Composable
fun GameContentPreview() {
    NQueensTheme {
        GameContent(
            size = 8,
            occupiedCells = BoardUiState.empty(8).occupiedCells,
            remainingQueens = 8,
            isVictory = false,
            onCellClicked = { _, _, _ -> },
            onClearButtonClick = {},
        )
    }
}