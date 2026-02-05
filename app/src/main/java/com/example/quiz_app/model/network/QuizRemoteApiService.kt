package com.example.quiz_app.model.network

import retrofit2.http.GET
import retrofit2.http.Query

interface QuizRemoteApiService {

    @GET("api.php")
    suspend fun getQuizQuestions(
        @Query("amount") amount: Int,
        @Query("category") category: Int? = null,
        @Query("difficulty") difficulty: String? = null,
        @Query("type") type: String? = null,
        @Query("encode") encode: String = "base64"

    ): QuizApiResponse
}