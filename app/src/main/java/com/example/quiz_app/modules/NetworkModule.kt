package com.example.quiz_app.di

import com.example.quiz_app.model.network.QuizRemoteApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://opentdb.com/")
            .addConverterFactory(
                Json { ignoreUnknownKeys = true }
                    .asConverterFactory(MediaType.get("application/json"))
            )
            .build()

    @Provides
    @Singleton
    fun provideQuizApi(retrofit: Retrofit): QuizRemoteApiService =
        retrofit.create(QuizRemoteApiService::class.java)
}