package com.example.quiz_app.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.quiz_app.model.QuizModel

/**
 * Button für Auswahloptionen (z. B. Kategorie, Schwierigkeit, Typ).
 * Hebt die aktuell ausgewählte Option farblich hervor
 * Nicht ausgewählte Optionen haben weißen Hintergrund und lila Rahmen
 */
@Composable
fun SelectionButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val purple = Color(0xFF800080)

    Button(
        onClick = onClick, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) purple else Color.White,
            contentColor = if (isSelected) Color.White else purple
        ), border = if (isSelected) null else BorderStroke(1.dp, purple)
    ) {
        Text(text)
    }
}

/**
 * Überschrift für Hauptbereiche der App (Titel "Quiz").
 */
@Composable
fun SectionHeader(title: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title, style = MaterialTheme.typography.headlineMedium, color = Color(0xFF458B74)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .height(3.dp)
                .fillMaxWidth(0.3f)
                .background(Color(0xFF458B74))
        )
    }

    /**
     * Stellt eine einzelne Quizfrage mit mehreren Antwortmöglichkeiten dar.
     * Zeigt die Frage
     * Listet alle Antwortbuttons
     * Speichert die ausgewählte Antwort direkt im QuizModel
     * Markiert die ausgewählte Antwort visuell
     */
}@Composable
fun QuestionItem(
    question: QuizModel,
    modifier: Modifier = Modifier
) {
    val purple = MaterialTheme.colorScheme.primary

    Column(
        modifier = modifier
            .background(
                color = Color.White,
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp)
    ) {
        Text(
            text = question.question,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(12.dp))

        val answers = remember(question) {
            (question.incorrectAnswers + question.correctAnswer).shuffled()
        }

        answers.forEach { answer ->
            val isSelected = question.userAnswer == answer

            Button(
                onClick = { question.userAnswer = answer },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) purple else Color.White,
                    contentColor = if (isSelected) Color.White else purple
                ),
                border = if (isSelected) null
                else BorderStroke(1.dp, purple)
            ) {
                Text(answer)
            }
        }
    }
}

/**
 * Kartenartige Überschrift für Auswahlbereiche
 * (z. B. Kategorie, Schwierigkeit, Fragentyp).
 * Dient zur visuellen Trennung der Sections
 */
@Composable
fun SectionHeaderCard(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFE6F0EC), shape = MaterialTheme.shapes.medium
            )
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {
        Text(
            text = title, style = MaterialTheme.typography.titleMedium, color = Color(0xFF458B74)
        )
    }
}


/**
 * Zeigt eine Liste von auswählbaren Optionen an.
 * Nutzt SelectionButton für jede Option
 * Gibt die Auswahl über onSelect an den Aufrufer zurück
 */
@Composable
fun SelectionSection(
    options: List<String>,
    selectedOption: String?,
    onSelect: (String) -> Unit,
) {
    Column {
        Spacer(Modifier.height(4.dp))
        options.forEach { option ->
            SelectionButton(option, selectedOption == option) { onSelect(option) }
        }
        Spacer(Modifier.height(12.dp))
    }
}

