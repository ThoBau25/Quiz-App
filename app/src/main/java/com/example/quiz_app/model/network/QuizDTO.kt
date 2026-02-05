package com.example.quiz_app.model.network


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuizApiResponse(
    @SerialName("response_code") val responseCode: Int,
    val results: List<QuizQuestionResponse>
)
@Serializable
data class QuizQuestionResponse(
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,
    @SerialName("correct_answer") val correctAnswer: String,
    @SerialName("incorrect_answers") val incorrectAnswers: List<String>
)