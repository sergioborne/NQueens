package com.sergioborne.nqueens.ui.board

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.sergioborne.nqueens.ui.theme.NQueensTheme
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GameScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    val stateFlow = MutableStateFlow(createState())
    val eventFlow = MutableSharedFlow<GameViewModel.Event>()

    private val viewModel: GameViewModel = mockk(relaxed = true) {
        every { uiState } returns stateFlow
        every { events } returns eventFlow
    }

    @Test
    fun whenCellIsClicked_onCellClickedIsCalledOnViewModel() {
        setContent()

        composeTestRule.onNodeWithTag("cell-2-3").performClick()

        verify {
            viewModel.onCellClicked(2, 3, any())
        }
    }

    @Test
    fun whenEventVictorySaved_onVictorySavedMethodIsCalled() = runTest {
        val onVictorySaved: () -> Unit = mockk(relaxed = true)

        setContent(
            onVictorySaved = onVictorySaved,
        )

        eventFlow.emit(GameViewModel.Event.VictorySaved)

        verify {
            onVictorySaved()
        }
    }

    @Test
    fun givenIsVictory_whenClickOnSave_onVictorySaveIsCalledOnViewModel() {
        val username = "Test name"
        setContent()
        stateFlow.tryEmit(
            createState(
                isVictory = true,
            )
        )
        composeTestRule.onNodeWithTag("victory-save-button").assertIsNotEnabled()
        composeTestRule.onNodeWithTag("victory-player-name").performTextInput(username)
        composeTestRule.onNodeWithTag("victory-save-button").assertIsEnabled()
        composeTestRule.onNodeWithTag("victory-save-button").performClick()

        verify {
            viewModel.onVictorySave(username)
        }
    }

    @Test
    fun whenClearIsClicked_onClearButtonClickedIsCalledOnViewModel() {
        setContent()

        composeTestRule.onNodeWithTag("clear-button").performClick()

        verify {
            viewModel.onClearButtonClicked()
        }
    }

    private fun setContent(
        onVictorySaved: () -> Unit = {}
    ) {
        composeTestRule.setContent {
            NQueensTheme {
                GameScreen(
                    viewModel = viewModel,
                    onVictorySaved = onVictorySaved,
                )
            }
        }
    }

    private fun createState(
        boardState: BoardUiState = BoardUiState.empty(8),
        remainingQueens: Int = 8,
        elapsedTime: Long = 0,
        isVictory: Boolean = false,
    ) = GameUiState(
        boardState = boardState,
        remainingQueens = remainingQueens,
        elapsedTime = elapsedTime,
        isVictory = isVictory,
    )
}

