package com.sergioborne.nqueens.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [ScoreEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class NQueensDatabase : RoomDatabase() {

    abstract val scoresDao: ScoresDao

    companion object {
        const val DATABASE_NAME = "nqueens_database"
    }
}