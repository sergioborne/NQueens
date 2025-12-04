package com.sergioborne.nqueens.ui.board

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sergioborne.nqueens.ui.BoardUiState
import com.sergioborne.nqueens.ui.theme.NQueensTheme

@Composable
fun ChessboardScreen(
    modifier: Modifier = Modifier,
    viewModel: BoardViewmodel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Chessboard(
        boardUiState = state,
        modifier = modifier,
        onCellClicked = viewModel::onCellClicked
    )
}

@Composable
fun Chessboard(
    boardUiState: BoardUiState,
    onCellClicked: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier) {
        val sizeModifier = if (maxWidth < maxHeight) {
            Modifier.fillMaxWidth()
        } else {
            Modifier.fillMaxHeight()
        }.aspectRatio(1f)

        Column(modifier = sizeModifier) {
            boardUiState.cells.mapIndexed { row, rowContent ->
                Row(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    rowContent.mapIndexed { column, cell ->
                        val color = if ((row + column) % 2 == 0) Color.White else Color.Black
                        Cell(
                            isQueen = cell.isQueen,
                            backgroundColor = color,
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            onClick = { onCellClicked(row, column) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChessboardPreview() {
    NQueensTheme {
        Chessboard(
            boardUiState = BoardUiState.empty(8)
                .changePosition(1, 1)
                .changePosition(2, 3),
            onCellClicked = { _, _ -> },
        )
    }
}
