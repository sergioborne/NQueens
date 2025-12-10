package com.sergioborne.nqueens.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BoardSizeScreen(
    onNavigateToBoard: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var size by remember { mutableStateOf("4") }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = size,
            onValueChange = { size = it },
            label = { Text("Chessboard Size") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Button(onClick = { onNavigateToBoard(size.toInt()) }) {
            Text("Play")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    BoardSizeScreen(onNavigateToBoard = { })
}
