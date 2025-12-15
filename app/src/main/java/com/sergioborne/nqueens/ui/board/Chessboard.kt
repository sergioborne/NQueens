package com.sergioborne.nqueens.ui.board

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sergioborne.nqueens.R
import com.sergioborne.nqueens.ui.theme.NQueensTheme
import com.sergioborne.nqueens.ui.utils.ThemePreviewsWithBackground
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun Chessboard(
    size: Int,
    occupiedCells: ImmutableList<CellUi>,
    onCellClicked: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .background(
                color = NQueensTheme.colors.chessBoard1,
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
                        val cell =
                            occupiedCells.firstOrNull { it.row == row && it.column == column }
                        val color = when {
                            cell?.isAttacked == true -> NQueensTheme.colors.attacked
                            ((row + column) % 2 == 0) -> NQueensTheme.colors.chessBoard1
                            else -> NQueensTheme.colors.chessBoard2
                        }
                        Cell(
                            isQueen = cell?.isQueen ?: false,
                            backgroundColor = color,
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .testTag("cell-$row-$column"),
                            onClick = { onCellClicked(row, column) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Cell(
    isQueen: Boolean,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .background(backgroundColor)
            .clickable {
                onClick.invoke()
            }
    ) {
        AnimatedVisibility(
            visible = isQueen,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
        ) {
            Queen(
                color = NQueensTheme.colors.queenIcon,
            )
        }
    }
}

@Composable
private fun Queen(
    modifier: Modifier = Modifier,
    color: Color
) {
    Image(
        painter = painterResource(id = R.drawable.ic_queen),
        contentDescription = stringResource(id = R.string.queen_content_description),
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp),
        colorFilter = ColorFilter.tint(color)
    )
}

@ThemePreviewsWithBackground
@Composable
fun ChessboardPreview() {
    NQueensTheme {
        Chessboard(
            size = 8,
            occupiedCells = listOf(
                CellUi(
                    row = 1,
                    column = 1,
                    isQueen = true,
                    isAttacked = false,
                ),
                CellUi(
                    row = 2,
                    column = 3,
                    isQueen = true,
                    isAttacked = false,
                ),
            ).toImmutableList(),
            onCellClicked = { _, _ -> },
        )
    }
}