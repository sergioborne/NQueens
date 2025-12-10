package com.sergioborne.nqueens.ui.victory

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sergioborne.nqueens.ui.theme.NQueensTheme

@Composable
fun VictoryScreen(
    onVictorySaved: (String) -> Unit,
) {
    var playerName by remember { mutableStateOf(TextFieldValue("")) }
    var showAnimation by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.Black.copy(alpha = 0.8f)
            )
            .padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedVisibility(
            visible = showAnimation,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            ConfettiAnimation(
                duration = 5000,
                onAnimationFinished = {
                    showAnimation = false
                }
            )
        }

        Column(
            modifier = Modifier
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Congratulations!",
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = playerName,
                onValueChange = { playerName = it },
                label = { Text("Enter your name:") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onVictorySaved(playerName.text)
                },
                enabled = playerName.text.isNotBlank()
            ) {
                Text("Save")
            }
        }
    }
}

@Preview
@Composable
private fun VictoryScreenPreview() {
    NQueensTheme {
        VictoryScreen(onVictorySaved = {})
    }
}