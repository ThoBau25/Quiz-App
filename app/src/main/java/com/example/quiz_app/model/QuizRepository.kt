package com.example.quiz_app.model

import com.example.quiz_app.model.db.HighscoreDao
import com.example.quiz_app.model.db.HighscoreEntity
import com.example.quiz_app.model.db.QuizDao
import com.example.quiz_app.model.db.QuizEntity
import com.example.quiz_app.model.network.QuizRemoteApiService
import kotlinx.coroutines.flow.map
import android.util.Base64
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Repräsentiert eine Quizfrage im UI.
 * Enthält alle relevanten Fragedaten
 * Speichert die vom Benutzer gewählte Antwort
 */
data class QuizModel(
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,
    val correctAnswer: String,
    val incorrectAnswers: List<String>,
) {
    var userAnswer by mutableStateOf<String?>(null)
}

/**
 * Flow mit allen Quizfragen aus der Datenbank.
 * Wandelt QuizEntity in QuizModel um
 * Wird von der UI beobachtet
 */
class QuizRepository(
    private val quizRemoteApiService: QuizRemoteApiService,
    private val quizDao: QuizDao,
    private val highscoreDao: HighscoreDao
) {
    val questions = quizDao.getAllQuestions().map { quizEntityList ->
        quizEntityList.map { entity ->
            QuizModel(
                entity.category,
                entity.type,
                entity.difficulty,
                entity.question,
                entity.correctAnswer,
                entity.incorrectAnswers,
            )
        }
    }
    val highscores = highscoreDao.getAllHighscores()

    /**
     * Speichert einen neuen Highscore in der Datenbank.
     */
    suspend fun addHighscore(score: Int, total: Int) {
        highscoreDao.insertHighscore(HighscoreEntity(score = score, total = total))
    }

    /**
     * Lädt Quizfragen von der OpenTrivia API,
     * dekodiert die Base64-Werte und speichert sie lokal.
     * Alte Fragen werden vorher gelöscht
     */
    suspend fun addFetchedQuestions(
        amount: Int = 10,
        category: Int? = null,
        difficulty: String? = null,
        type: String? = null,
    ) {

        val quizResponse = quizRemoteApiService.getQuizQuestions(
            amount, category, difficulty, type, encode = "base64"
        )

        val quizEntities = quizResponse.results.map { dto ->
            QuizEntity(
                id = 0,
                category = decodeBase64(dto.category),
                type = decodeBase64(dto.type),
                difficulty = decodeBase64(dto.difficulty),
                question = decodeBase64(dto.question),
                correctAnswer = decodeBase64(dto.correctAnswer),
                incorrectAnswers = dto.incorrectAnswers.map { decodeBase64(it) }
            )
        }

        quizDao.clearAll()
        quizDao.insertQuestions(quizEntities)

    }

    /**
     * Dekodiert Base64-kodierte Strings aus der API.
     */
    private fun decodeBase64(text: String): String {
        return String(Base64.decode(text, Base64.DEFAULT))
    }

}

