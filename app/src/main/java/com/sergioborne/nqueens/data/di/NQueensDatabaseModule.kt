package com.sergioborne.nqueens.data.di

import android.content.Context
import androidx.room.Room
import com.sergioborne.nqueens.data.NQueensDatabase
import com.sergioborne.nqueens.data.ScoresDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NQueensDatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): NQueensDatabase = Room.databaseBuilder(
        context,
        NQueensDatabase::class.java,
        NQueensDatabase.DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideScoresDao(
        db: NQueensDatabase
    ): ScoresDao = db.scoresDao
}