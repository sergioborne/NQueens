package com.sergioborne.nqueens.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.sergioborne.nqueens.ui.game.GameNavigation
import com.sergioborne.nqueens.ui.leaderboard.LeaderboardScreen
import com.sergioborne.nqueens.ui.theme.NQueensTheme
import com.sergioborne.nqueens.ui.utils.ThemePreviewsWithBackground

@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(MainDestinations.GameScreen)

    val currentKey = rememberSaveable(stateSaver = MainDestinations.stateSaver) {
        mutableStateOf(MainDestinations.GameScreen)
    }

    var bottomBarVisible by remember { mutableStateOf(true) }

    Scaffold(
        modifier = Modifier.background(NQueensTheme.colors.background),
        bottomBar = {
            AnimatedVisibility(
                visible = bottomBarVisible,
                exit = slideOutVertically { it },
                enter = slideInVertically { it },
            ) {
                BottomBarNavigation(
                    currentKey = currentKey.value,
                    onItemClick = { key ->
                        backStack.navigateToKey(currentKey, key)
                    },
                )
            }
        }
    ) { innerPadding ->
        NavDisplay(
            modifier = Modifier
                .background(NQueensTheme.colors.background)
                .padding(innerPadding),
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
                    LeaderboardScreen(
                        onPlayClicked = {
                            backStack.navigateToKey(currentKey, MainDestinations.GameScreen)
                        }
                    )
                }
            },
            transitionSpec = {
                ContentTransform(
                    targetContentEnter = fadeIn(tween(300)),
                    initialContentExit = fadeOut(tween(300))
                )
            }
        )
    }
}

@Composable
private fun BottomBarNavigation(
    currentKey: MainDestinations,
    onItemClick: (MainDestinations) -> Unit,
) {
    NavigationBar(
        containerColor = NQueensTheme.colors.surfaceContainer,
        contentColor = NQueensTheme.colors.onSurfaceContainer,
    ) {
        MainDestinations.items.forEach { key ->
            NavigationBarItem(
                selected = key == currentKey,
                onClick = {
                    onItemClick(key)
                },
                icon = {
                    Icon(
                        painter = painterResource(key.iconRes),
                        contentDescription = key.label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(key.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NQueensTheme.colors.navigationBarSelectedItemColor,
                    selectedTextColor = NQueensTheme.colors.navigationBarSelectedItemColor,
                    unselectedIconColor = NQueensTheme.colors.navigationBarItemColor,
                    unselectedTextColor = NQueensTheme.colors.navigationBarItemColor,
                    indicatorColor = NQueensTheme.colors.navigationBarIndicatorColor,
                )
            )
        }
    }
}

private fun NavBackStack<NavKey>.navigateToKey(
    currentKey: MutableState<MainDestinations>,
    keyToNavigate: MainDestinations,
) {
    if (currentKey.value != keyToNavigate) {
        currentKey.value = keyToNavigate
        clear()
        add(keyToNavigate)
    }
}

@ThemePreviewsWithBackground
@Composable
private fun BottomBarNavigationPreview() {
    NQueensTheme {
        BottomBarNavigation(MainDestinations.GameScreen) { }
    }
}
