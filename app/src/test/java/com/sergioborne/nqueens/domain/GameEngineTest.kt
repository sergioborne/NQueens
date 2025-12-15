package com.sergioborne.nqueens.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GameEngineTest {

    private lateinit var gameEngine: GameEngine

    @Before
    fun setUp() {
        gameEngine = GameEngine()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `changePosition throws exception if not initialized`() {
        gameEngine.changePosition(0, 0)
    }

    @Test
    fun `changePosition adds a queen`() {
        gameEngine.initGameEngine(8)
        val result = gameEngine.changePosition(0, 0)
        val expected = listOf(CellData(row = 0, column = 0, isQueen = true, isAttacked = false))
        assertEquals(
            expected,
            result,
        )
    }

    @Test
    fun `changePosition removes a queen`() {
        gameEngine.initGameEngine(8)
        gameEngine.changePosition(0, 0)
        val result = gameEngine.changePosition(0, 0)
        assertEquals(
            emptyList<CellData>(),
            result,
        )
    }

    @Test
    fun `queens on same row are attacked`() {
        gameEngine.initGameEngine(8)
        gameEngine.changePosition(0, 0)
        val result = gameEngine.changePosition(0, 1)

        val queen1 = result.find { it.row == 0 && it.column == 0 }
        val queen2 = result.find { it.row == 0 && it.column == 1 }

        assertTrue(queen1!!.isAttacked)
        assertTrue(queen2!!.isAttacked)
    }

    @Test
    fun `queens on same column are attacked`() {
        gameEngine.initGameEngine(8)
        gameEngine.changePosition(0, 0)
        val result = gameEngine.changePosition(1, 0)

        val queen1 = result.find { it.row == 0 && it.column == 0 }
        val queen2 = result.find { it.row == 1 && it.column == 0 }

        assertTrue(queen1!!.isAttacked)
        assertTrue(queen2!!.isAttacked)
    }

    @Test
    fun `queens on same diagonal are attacked`() {
        gameEngine.initGameEngine(8)
        gameEngine.changePosition(0, 0)
        val result = gameEngine.changePosition(1, 1)

        val queen1 = result.find { it.row == 0 && it.column == 0 }
        val queen2 = result.find { it.row == 1 && it.column == 1 }

        assertTrue(queen1!!.isAttacked)
        assertTrue(queen2!!.isAttacked)
    }

    @Test
    fun `queens on same anti-diagonal are attacked`() {
        gameEngine.initGameEngine(8)
        gameEngine.changePosition(0, 1)
        val result = gameEngine.changePosition(1, 0)

        val queen1 = result.find { it.row == 0 && it.column == 1 }
        val queen2 = result.find { it.row == 1 && it.column == 0 }

        assertTrue(queen1!!.isAttacked)
        assertTrue(queen2!!.isAttacked)
    }

    @Test
    fun `queens not in conflict are not attacked`() {
        gameEngine.initGameEngine(8)
        gameEngine.changePosition(0, 0)
        val result = gameEngine.changePosition(1, 2)

        val queen1 = result.find { it.row == 0 && it.column == 0 }
        val queen2 = result.find { it.row == 1 && it.column == 2 }

        assertFalse(queen1!!.isAttacked)
        assertFalse(queen2!!.isAttacked)
    }
}
