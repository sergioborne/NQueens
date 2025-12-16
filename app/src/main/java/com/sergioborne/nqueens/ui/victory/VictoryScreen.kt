package com.sergioborne.nqueens.ui.victory

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults.colors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.sergioborne.nqueens.ui.theme.NQueensTheme
import com.sergioborne.nqueens.ui.utils.ThemePreviewsWithBackground

@Composable
fun VictoryScreen(
    onVictorySaved: (String) -> Unit,
) {
    var playerName by rememberSaveable { mutableStateOf("") }
    var showAnimation by rememberSaveable { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.Black.copy(alpha = 0.8f)
            )
            .padding(32.dp)
            .clickable(onClick = {}, enabled = false),
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
                    color = NQueensTheme.colors.surfaceContainer,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Congratulations!",
                color = NQueensTheme.colors.textPrimary,
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = playerName,
                onValueChange = { playerName = it },
                label = { Text("Enter your name:") },
                singleLine = true,
                colors = colors().copy(
                    focusedTextColor = NQueensTheme.colors.textPrimary,
                    unfocusedTextColor = NQueensTheme.colors.textPrimary,
                    unfocusedLabelColor = NQueensTheme.colors.textSecondary,
                    focusedLabelColor = NQueensTheme.colors.textSecondary,
                ),
                modifier = Modifier.testTag("victory-player-name"),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onVictorySaved(playerName)
                },
                enabled = playerName.isNotBlank(),
                colors = buttonColors(),
                modifier = Modifier.testTag("victory-save-button")
            ) {
                Text("Save")
            }
        }
    }
}

@ThemePreviewsWithBackground
@Composable
private fun VictoryScreenPreview() {
    NQueensTheme {
        VictoryScreen(onVictorySaved = {})
    }
}