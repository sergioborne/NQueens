package com.sergioborne.nqueens.ui.leaderboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sergioborne.nqueens.ui.theme.NQueensTheme
import com.sergioborne.nqueens.ui.utils.formatPreciseTime
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun LeaderboardScreen() {
    val viewModel = hiltViewModel<LeaderboardViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    LeaderboardContent(
        state = state.value,
        onClearLeaderboardClick = viewModel::clearLeaderboardClick,
    )
}

@Composable
fun LeaderboardContent(
    state: LeaderboardUiState,
    onClearLeaderboardClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Leaderboard",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        LeaderboardHeader()
        when (state) {
            is LeaderboardUiState.Content -> {
                LeaderboardList(
                    leaderboardEntries = state.leaderboardEntries,
                    onClearLeaderboardClick = onClearLeaderboardClick,
                )
            }

            is LeaderboardUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.padding(32.dp)
                )
            }
        }

    }
}


@Composable
fun LeaderboardList(
    leaderboardEntries: ImmutableList<LeaderboardEntry>,
    onClearLeaderboardClick: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        itemsIndexed(
            items = leaderboardEntries,
            key = { position, entry -> position }
        ) { position, entry ->
            LeaderboardListItem(
                modifier = Modifier.animateItem(),
                entry = entry,
            )
        }
        if (leaderboardEntries.isNotEmpty()) {
            item {
                Button(
                    modifier = Modifier.padding(32.dp),
                    onClick = onClearLeaderboardClick,
                ) {
                    Text(text = "Clear Leaderboard")
                }
            }
        }
    }
}

@Composable
fun LeaderboardHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Name", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
        Text(
            text = "Board Size",
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Time",
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End
        )
    }
}


@Composable
fun LeaderboardListItem(
    entry: LeaderboardEntry,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = entry.name, modifier = Modifier.weight(1f))
            Text(
                text = entry.boardSize.toString(),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Text(
                text = entry.timeTakenMillis.formatPreciseTime(),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LeaderboardScreenPreview() {
    NQueensTheme {
        LeaderboardContent(
            state = LeaderboardUiState.Content(
                leaderboardEntries = persistentListOf(
                    LeaderboardEntry(
                        name = "John Doe",
                        boardSize = 8,
                        timeTakenMillis = 35000,
                    ),
                    LeaderboardEntry(
                        name = "Jane Smith",
                        boardSize = 12,
                        timeTakenMillis = 42000,
                    ),
                    LeaderboardEntry(
                        name = "Bob Johnson",
                        boardSize = 10,
                        timeTakenMillis = 38000,
                    )
                ),
            ),
            onClearLeaderboardClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LeaderboardScreenLoadingPreview() {
    NQueensTheme {
        LeaderboardContent(
            state = LeaderboardUiState.Loading,
            onClearLeaderboardClick = {},
        )
    }
}