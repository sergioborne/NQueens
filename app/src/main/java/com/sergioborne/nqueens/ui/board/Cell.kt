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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sergioborne.nqueens.R

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
                color = Color.Green
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