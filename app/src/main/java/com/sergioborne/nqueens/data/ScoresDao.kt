package com.sergioborne.nqueens.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoresDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(score: ScoreEntity)

    @Query("SELECT * FROM scores")
    fun getAllScores(): Flow<List<ScoreEntity>>

    @Query("DELETE FROM scores")
    suspend fun deleteAll()
}