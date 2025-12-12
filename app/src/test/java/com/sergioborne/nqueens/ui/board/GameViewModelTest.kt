package com.sergioborne.nqueens.ui.board

import app.cash.turbine.test
import com.sergioborne.nqueens.domain.Score
import com.sergioborne.nqueens.repository.LeaderboardRepository
import com.sergioborne.nqueens.utils.MainDispatcherRule
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GameViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: GameViewModel
    private val leaderboardRepository: LeaderboardRepository = mockk(relaxed = true)
    private val boardSize = 4

    @Before
    fun setUp() {
        viewModel = GameViewModel(boardSize, leaderboardRepository)
    }

    @Test
    fun `initial state is correct`() = runTest {
        val state = viewModel.uiState.value
        assertEquals(boardSize, state.boardState.size)
        assertEquals(boardSize, state.remainingQueens)
        assertFalse(state.isVictory)
        assertEquals(0, state.elapsedTime)
    }

    @Test
    fun `onCellClicked updates board and remaining queens`() = runTest {
        viewModel.onCellClicked(0, 0, timeElapsed = 10L)
        val state = viewModel.uiState.value
        assertTrue(state.boardState.cells[0].isQueen)
        assertEquals(boardSize - 1, state.remainingQueens)
        assertFalse(state.isVictory)
    }

    @Test
    fun `onCellClicked stops timer on victory`() = runTest {
        val name = "name"
        val scoreSlot = slot<Score>()
        viewModel.onCellClicked(1, 0, timeElapsed = 10L)
        viewModel.onCellClicked(3, 1, timeElapsed = 20L)
        viewModel.onCellClicked(0, 2, timeElapsed = 30L)
        viewModel.onCellClicked(2, 3, timeElapsed = 40L)
        viewModel.onVictorySave(name)

        coVerify {
            leaderboardRepository.insertScore(capture(scoreSlot))
        }

        assertTrue(viewModel.uiState.value.isVictory)
        assertEquals(0, viewModel.uiState.value.remainingQueens)
        assertEquals(name, scoreSlot.captured.name)
        assertEquals(40L, scoreSlot.captured.time)
    }

    @Test
    fun `onClearButtonClicked resets the board`() = runTest(UnconfinedTestDispatcher()) {
        viewModel.onCellClicked(0, 0, timeElapsed = 100L)
        viewModel.onClearButtonClicked()
        val state = viewModel.uiState.value

        assertTrue(state.boardState.cells.none { it.isQueen })
        assertEquals(boardSize, state.remainingQueens)
        assertFalse(state.isVictory)
    }

    @Test
    fun `onVictorySave inserts score and emits event`() = runTest {
        viewModel.events.test {
            viewModel.onVictorySave("Test Player")
            assertEquals(GameViewModel.Event.VictorySaved, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { leaderboardRepository.insertScore(any<Score>()) }
    }
}