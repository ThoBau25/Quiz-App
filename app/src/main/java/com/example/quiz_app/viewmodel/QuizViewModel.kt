package com.example.quiz_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiz_app.model.QuizModel
import com.example.quiz_app.model.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI-Zustand für den QuizScreen.
 * Enthält alle aktuell angezeigten Fragen
 * Steuert Ladezustand und Fehlermeldungen
 */

data class QuizUiState(
    val questions: List<QuizModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

/**
 * ViewModel für den Quiz-Bildschirm.
 * Verwaltet den UI-Zustand
 * Kommuniziert mit dem QuizRepository
 * Enthält die gesamte Geschäftslogik des Quiz
 */
@HiltViewModel
class QuizViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
) : ViewModel() {

    private val _quizUiState = MutableStateFlow(QuizUiState())
    val quizUiState = _quizUiState.asStateFlow()

    val highscores = quizRepository.highscores


    /**
     * Lädt Quizfragen basierend auf den gewählten Einstellungen.
     * Holt Daten von der API
     * Speichert sie in der Datenbank
     * Aktualisiert anschließend den UI-State
     */
    fun fetchQuestions(
        amount: Int,
        category: Int,
        difficulty: String?,
        type: String?
    ) {
        viewModelScope.launch {
            try {
                _quizUiState.value = _quizUiState.value.copy(isLoading = true, errorMessage = null)

                // Nur einmal Fragen abrufen und in DB speichern
                quizRepository.addFetchedQuestions(amount, category, difficulty, type)

                // Danach einmal die Fragen aus der DB holen
                val newQuestions = quizRepository.questions.firstOrNull() ?: emptyList()
                _quizUiState.value = _quizUiState.value.copy(
                    questions = newQuestions,
                    isLoading = false
                )
            } catch (e: Exception) {
                _quizUiState.value = _quizUiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }

    /**
     * Setzt das Quiz zurück.
     * Entfernt alle geladenen Fragen aus dem UI-State
     */
    fun resetQuiz() {
        _quizUiState.value = QuizUiState()
    }

    /**
     * Berechnet die erreichte Punktezahl.
     * Zählt korrekt beantwortete Fragen
     */
    fun evaluateScore(): Int {
        return _quizUiState.value.questions.count { q -> q.userAnswer == q.correctAnswer }
    }

    /**
     * Berechnet den Score und speichert ihn als Highscore.
     */
    fun evaluateScoreAndSave() {
        val score = evaluateScore()
        viewModelScope.launch {
            quizRepository.addHighscore(score, _quizUiState.value.questions.size)
        }
    }
}