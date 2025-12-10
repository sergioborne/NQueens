package com.sergioborne.nqueens.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.sergioborne.nqueens.ui.game.GameNavigation
import com.sergioborne.nqueens.ui.leaderboard.LeaderboardScreen
import kotlin.text.clear

@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(MainDestinations.GameScreen)

    val currentKey = rememberSaveable(stateSaver = MainDestinations.stateSaver) {
        mutableStateOf(MainDestinations.GameScreen)
    }

    var bottomBarVisible by remember { mutableStateOf(true) }

    Scaffold(
        bottomBar = {
            if (bottomBarVisible) {
                BottomBarNavigation(currentKey, onItemClick = { key ->
                    backStack.apply {
                        clear()
                        add(key)
                    }
                })
            }
        }
    ) { innerPadding ->
        NavDisplay(
            modifier = Modifier.padding(innerPadding),
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<MainDestinations.GameScreen> {
                    GameNavigation(
                        onGameStarted = {
                            bottomBarVisible = false
                        },
                        onGameFinished = {
                            bottomBarVisible = true
                        },
                    )
                }

                entry<MainDestinations.LeaderboardScreen> {
                    LeaderboardScreen()
                }
            }
        )
    }
}

@Composable
private fun BottomBarNavigation(
    currentKey: MutableState<MainDestinations>,
    onItemClick: (MainDestinations) -> Unit,
) {
    NavigationBar {
        MainDestinations.items.forEach { key ->
            NavigationBarItem(
                selected = key == currentKey,
                onClick = {
                    if (currentKey != key) {
                        currentKey.value = key
                        onItemClick(key)
                    }
                },
                icon = {
                    Icon(
                        painterResource(key.iconRes),
                        contentDescription = key.label
                    )
                },
                label = { Text(key.label) }
            )
        }
    }
}
