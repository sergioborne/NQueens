package com.sergioborne.nqueens.repository

import com.sergioborne.nqueens.data.ScoreEntity
import com.sergioborne.nqueens.data.ScoresDao
import com.sergioborne.nqueens.domain.Score
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LeaderboardRepository @Inject constructor(
    private val scoresDao: ScoresDao,
) {

    suspend fun insertScore(score: Score) = scoresDao.insert(score.toEntity())

    fun getAllScores(): Flow<List<ScoreEntity>> = scoresDao.getAllScores()

    suspend fun clear() = scoresDao.deleteAll()

    private fun Score.toEntity() = ScoreEntity(
        name = name,
        time = time,
        boardSize = boardSize,
        date = date,
    )

}