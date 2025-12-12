package com.sergioborne.nqueens.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sergioborne.nqueens.ui.components.NumberPicker

@Composable
fun BoardSizeScreen(
    onNavigateToBoard: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var size by remember { mutableIntStateOf(4) }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Select Board Size:",
            fontSize = 32.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        NumberPicker(
            from = 4,
            to = 20,
            fontSize = 40.sp,
            onNumberSelected = { size = it },
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { onNavigateToBoard(size) }) {
            Text("Play")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    BoardSizeScreen(onNavigateToBoard = { })
}
