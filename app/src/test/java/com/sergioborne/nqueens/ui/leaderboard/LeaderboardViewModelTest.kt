package com.sergioborne.nqueens.ui.leaderboard

import app.cash.turbine.test
import com.sergioborne.nqueens.data.ScoreEntity
import com.sergioborne.nqueens.repository.LeaderboardRepository
import com.sergioborne.nqueens.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date

@ExperimentalCoroutinesApi
class LeaderboardViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val leaderboardRepository: LeaderboardRepository = mockk(relaxed = true)

    private lateinit var viewModel: LeaderboardViewModel

    @Test
    fun `initial state is Loading`() = runTest {
        viewModel = LeaderboardViewModel(leaderboardRepository)
        viewModel.uiState.test {
            assertEquals(LeaderboardUiState.Loading, awaitItem())
        }

    }

    @Test
    fun `given no scores, state is Empty`() = runTest {
        every { leaderboardRepository.getAllScores() } returns flowOf(emptyList())
        viewModel = LeaderboardViewModel(leaderboardRepository)
        advanceUntilIdle()
        assertEquals(LeaderboardUiState.Empty, viewModel.uiState.value)
    }

    @Test
    fun `scores are sorted by board size and then time`() = runTest {
        val scores = listOf(
            ScoreEntity(1, "Player A", 1000, 4, Date(0)),
            ScoreEntity(2, "Player B", 1200, 4, Date(0)),
            ScoreEntity(3, "Player C", 900, 5, Date(0)),
            ScoreEntity(4, "Player D", 1100, 5, Date(0)),
            ScoreEntity(5, "Player E", 1000, 5, Date(0)),
            ScoreEntity(6, "Player F", 1300, 4, Date(0)),
        )
        every { leaderboardRepository.getAllScores() } returns flowOf(scores)
        viewModel = LeaderboardViewModel(leaderboardRepository)

        viewModel.uiState.test {
            val contentState = awaitItem() as LeaderboardUiState.Content
            val entries = contentState.leaderboardEntries
            assertEquals(6, entries.size)
            // 4x4 scores
            assertEquals(4, entries[0].boardSize)
            assertEquals(1000, entries[0].timeTakenMillis)
            assertEquals(4, entries[1].boardSize)
            assertEquals(1200, entries[1].timeTakenMillis)
            assertEquals(4, entries[2].boardSize)
            assertEquals(1300, entries[2].timeTakenMillis)
            // 5x5 scores
            assertEquals(5, entries[3].boardSize)
            assertEquals(900, entries[3].timeTakenMillis)
            assertEquals(5, entries[4].boardSize)
            assertEquals(1000, entries[4].timeTakenMillis)
            assertEquals(5, entries[5].boardSize)
            assertEquals(1100, entries[5].timeTakenMillis)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `clearLeaderboardClick clears the leaderboard`() = runTest {
        coEvery { leaderboardRepository.clear() } returns Unit
        every { leaderboardRepository.getAllScores() } returns flowOf(emptyList())
        viewModel = LeaderboardViewModel(leaderboardRepository)
        viewModel.clearLeaderboardClick()
        advanceUntilIdle()
        coVerify { leaderboardRepository.clear() }
    }
}
