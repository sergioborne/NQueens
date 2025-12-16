package com.sergioborne.nqueens.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
class ScoresDaoTest {
    private lateinit var scoresDao: ScoresDao
    private lateinit var nQueensDatabase: NQueensDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        nQueensDatabase = Room.inMemoryDatabaseBuilder(
            context, NQueensDatabase::class.java
        ).build()
        scoresDao = nQueensDatabase.scoresDao
    }

    @After
    fun closeDb() {
        nQueensDatabase.close()
    }

    @Test
    fun insert_and_get_ordered_scores() = runTest {
        val score1 = ScoreEntity(1, "Player1", 10, 8, Date())
        val score2 = ScoreEntity(1, "Player2", 20, 4, Date())
        val score3 = ScoreEntity(3, "Player2", 15, 4, Date())

        scoresDao.insert(score1)
        scoresDao.insert(score2)
        scoresDao.insert(score3)

        scoresDao.getAllScores().test {
            assertEquals(listOf(score2, score3, score1), awaitItem())
        }

    }

    @Test
    fun clear_scores() = runTest {
        val score1 = ScoreEntity(1, "Player1", 10, 8, Date())
        scoresDao.insert(score1)

        scoresDao.getAllScores().test {
            awaitItem().isNotEmpty()
        }

        scoresDao.deleteAll()

        scoresDao.getAllScores().test {
            awaitItem().isEmpty()
        }
    }
}