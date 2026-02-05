package com.example.quiz_app.modules

import com.example.quiz_app.model.QuizRepository
import com.example.quiz_app.model.db.HighscoreDao
import com.example.quiz_app.model.db.QuizDao
import com.example.quiz_app.model.network.QuizRemoteApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideQuizRepository(
        api: QuizRemoteApiService,quizDao: QuizDao,highscoreDao: HighscoreDao): QuizRepository{
        return QuizRepository(api,quizDao,highscoreDao)
    }
}