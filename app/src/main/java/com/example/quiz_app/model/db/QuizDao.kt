package com.example.quiz_app.model.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {

    @Query("SELECT * FROM quiz_table")
    fun getAllQuestions(): Flow<List<QuizEntity>>

    // Falls ein Eintrag bereits mit der gleichen ID existiert wird hier der Eintrag überschrieben und wirft keine Exception
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuizEntity>)

    @Query("DELETE FROM quiz_table")
    suspend fun clearAll()
}

@Dao
interface HighscoreDao {
    @Insert
    suspend fun insertHighscore(highscore: HighscoreEntity)

    @Query("SELECT * FROM highscore_table ORDER BY score DESC")
    fun getAllHighscores(): Flow<List<HighscoreEntity>>
}