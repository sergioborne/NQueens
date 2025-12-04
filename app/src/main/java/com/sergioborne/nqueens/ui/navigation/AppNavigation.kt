package com.sergioborne.nqueens.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.sergioborne.nqueens.ui.board.GameScreen
import com.sergioborne.nqueens.ui.board.GameViewModel
import com.sergioborne.nqueens.ui.main.MainScreen

@Composable
fun AppNavigation() {
    val backStack = remember { mutableStateListOf<Any>(MainDestinations.MainScreen) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<MainDestinations.MainScreen> {
                MainScreen(onNavigateToBoard = {
                    backStack.add(MainDestinations.ChessboardScreen(it))
                })
            }

            entry<MainDestinations.ChessboardScreen> { key ->
                val viewModel = hiltViewModel<GameViewModel, GameViewModel.Factory>(
                    creationCallback = { factory ->
                        factory.create(key.boardSize)
                    }
                )
                GameScreen(viewModel = viewModel, onVictoryAnimationFinished = {
                    backStack.removeLastOrNull()
                })
            }
        }
    )
}
