package com.sergioborne.nqueens.ui.components

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

/*
    A Number Picker Composable that allows users to pick a number from a given range.
    It uses a LazyColumn with snapping behavior to center the selected number.
    Got the code from here: https://shift2dev.com/create-a-custom-android-number-picker-in-compose/
    and modified it.
*/

@Composable
fun HorizontalNumberPicker(
    from: Int,
    to: Int,
    fontSize: TextUnit,
    onNumberSelected: (Int) -> Unit,
    width: Dp,
    textColor: Color,
) {
    val values = remember {
        (from..to).toList()
    }
    val lazyListState = rememberLazyListState(initialFirstVisibleItemIndex = 0)
    val flingBehavior =
        rememberSnapFlingBehavior(
            lazyListState = lazyListState,
            snapPosition = SnapPosition.Center,
        )

    val centeredItemIndex = remember {
        derivedStateOf {
            if (!lazyListState.isScrollInProgress) {
                lazyListState.firstVisibleItemIndex
            } else {
                -1
            }
        }
    }

    LaunchedEffect(centeredItemIndex.value) {
        val selectedIndex = centeredItemIndex.value
        if (selectedIndex != -1 && selectedIndex < values.size) {
            onNumberSelected(values[selectedIndex])
        }
    }

    Box(modifier = Modifier.width(width)) {
        val itemWidth = with(LocalDensity.current) {
            fontSize.toDp()
        }
        val listWidth = width
        LazyRow(
            state = lazyListState,
            flingBehavior = flingBehavior,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(5.dp)
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.horizontalGradient(
                            0f to Color.Transparent,
                            0.2f to textColor.copy(alpha = 0.2f),
                            0.5f to textColor,
                            0.8f to textColor.copy(alpha = 0.2f),
                            1f to Color.Transparent
                        ),
                        blendMode = BlendMode.DstIn
                    )
                },
            contentPadding = PaddingValues(horizontal = listWidth / 2 - itemWidth / 2)
        ) {
            items(values) { value ->
                Text(
                    modifier = Modifier.padding(vertical = 5.dp, horizontal = 9.dp),
                    text = "$value",
                    fontSize = fontSize,
                    color = textColor,
                )
            }
        }
    }
}