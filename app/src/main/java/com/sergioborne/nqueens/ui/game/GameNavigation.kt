package com.sergioborne.nqueens.ui.game

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.sergioborne.nqueens.ui.board.GameScreen
import com.sergioborne.nqueens.ui.board.GameViewModel

@Composable
fun GameNavigation(
    onGameStarted: () -> Unit,
    onGameFinished: () -> Unit,
) {
    val backStack = rememberNavBackStack(GameDestinations.BoardSizeScreen)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<GameDestinations.BoardSizeScreen> {
                onGameFinished()
                BoardSizeScreen(onNavigateToBoard = {
                    backStack.add(GameDestinations.ChessboardScreen(it))
                })
            }

            entry<GameDestinations.ChessboardScreen> { key ->
                onGameStarted()
                val viewModel = hiltViewModel<GameViewModel, GameViewModel.Factory>(
                    creationCallback = { factory ->
                        factory.create(key.boardSize)
                    }
                )
                GameScreen(viewModel = viewModel, onVictorySaved = {
                    backStack.removeLastOrNull()

                })
            }
        }
    )
}
