package com.sergioborne.nqueens.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sergioborne.nqueens.ui.components.HorizontalNumberPicker
import com.sergioborne.nqueens.ui.theme.NQueensTheme
import com.sergioborne.nqueens.ui.utils.ThemePreviewsWithBackground

@Composable
fun BoardSizeScreen(
    onNavigateToBoard: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var size by remember { mutableIntStateOf(4) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NQueensTheme.colors.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Select Board Size:",
            fontSize = 32.sp,
            color = NQueensTheme.colors.textPrimary,
        )
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalNumberPicker(
            from = 4,
            to = 20,
            fontSize = 50.sp,
            onNumberSelected = { size = it },
            width = 200.dp,
            textColor = NQueensTheme.colors.textPrimary,
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { onNavigateToBoard(size) }) {
            Text("Play")
        }
    }
}

@ThemePreviewsWithBackground
@Composable
fun MainScreenPreview() {
    NQueensTheme {
        BoardSizeScreen(onNavigateToBoard = { })
    }
}
