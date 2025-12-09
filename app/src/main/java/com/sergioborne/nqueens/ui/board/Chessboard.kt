package com.sergioborne.nqueens.ui.board

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sergioborne.nqueens.ui.theme.NQueensTheme
import kotlinx.collections.immutable.ImmutableList

@Composable
fun Chessboard(
    size: Int,
    cells: ImmutableList<CellUi>,
    onCellClicked: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .padding(16.dp)
            .background(
                color = Color.Black,
            )
            .padding(2.dp)
    ) {
        val sizeModifier = if (maxWidth < maxHeight) {
            Modifier.fillMaxWidth()
        } else {
            Modifier.fillMaxHeight()
        }.aspectRatio(1f)

        Column(modifier = sizeModifier) {
            repeat(size) { row ->
                Row(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    repeat(size) { column ->
                        val cell = cells[row * size + column]
                        val color = when {
                            cell.isAttacked -> Color.Red
                            ((row + column) % 2 == 0) -> Color.White
                            else -> Color.Black
                        }
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
            size = 8,
            cells = BoardUiState.empty(8)
                .changePosition(1, 1)
                .changePosition(2, 3).cells,
            onCellClicked = { _, _ -> },
        )
    }
}