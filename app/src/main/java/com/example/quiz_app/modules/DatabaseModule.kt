package com.example.quiz_app.modules

import android.app.Application
import com.example.quiz_app.model.db.HighscoreDao
import com.example.quiz_app.model.db.QuizDao
import com.example.quiz_app.model.db.QuizDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): QuizDatabase = QuizDatabase.getDatabase(app)

    @Provides
    @Singleton
    fun provideQuizDao(db: QuizDatabase): QuizDao = db.quizDao()

    @Provides
    @Singleton
    fun provideHighscoreDao(db: QuizDatabase): HighscoreDao = db.highscoreDao()
}