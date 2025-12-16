package com.sergioborne.nqueens.ui.leaderboard

import app.cash.turbine.test
import com.sergioborne.nqueens.data.ScoreEntity
import com.sergioborne.nqueens.repository.LeaderboardRepository
import com.sergioborne.nqueens.utils.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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
    fun `given scores are available, state is ShowLeaderboard with sorted scores`() = runTest {
        val scores = listOf(
            ScoreEntity(1, "PlayerB", 10, 8, Date()),
            ScoreEntity(2, "PlayerA", 15, 4, Date()),
            ScoreEntity(3, "PlayerC", 20, 4, Date())
        )
        every { leaderboardRepository.getAllScores() } returns flowOf(scores)
        viewModel = LeaderboardViewModel(leaderboardRepository)
        advanceUntilIdle()

        val uiState = viewModel.uiState.value
        assert(uiState is LeaderboardUiState.Content)

        val leaderboard = (uiState as LeaderboardUiState.Content).leaderboardEntries

        val expectedLeaderboard = listOf(
            LeaderboardEntry("PlayerB", 8, 10),
            LeaderboardEntry( "PlayerA", 4, 15),
            LeaderboardEntry( "PlayerC", 4, 20)
        )

        assertEquals(expectedLeaderboard, leaderboard)
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
