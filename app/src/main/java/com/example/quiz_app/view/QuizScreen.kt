package com.example.quiz_app.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.quiz_app.components.QuestionItem
import com.example.quiz_app.components.SectionHeader
import com.example.quiz_app.components.SectionHeaderCard
import com.example.quiz_app.components.SelectionSection
import com.example.quiz_app.viewmodel.QuizViewModel

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    onNavigatetoHighscore: () -> Unit,
) {
    val uiState by viewModel.quizUiState.collectAsState()

    val listState = rememberLazyListState()

    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var selectedDifficulty by remember { mutableStateOf<String?>(null) }
    var selectedType by remember { mutableStateOf<String?>(null) }

    var showResultDialog by remember { mutableStateOf(false) }
    var lastScore by remember { mutableStateOf(0) }

    val showQuestions = !uiState.isLoading && uiState.questions.isNotEmpty()

    val showLoadButton =
        selectedCategory != null &&
                selectedDifficulty != null &&
                selectedType != null

    val loadButtonIndex = 1000

    LaunchedEffect(showLoadButton) {
        if (showLoadButton) {
            listState.animateScrollToItem(loadButtonIndex)
        }else{
            listState.animateScrollToItem(0)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Spacer(modifier = Modifier.height(30.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionHeader("Quiz")

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = onNavigatetoHighscore,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF458B74),
                            contentColor = Color.White
                        )
                    ) {
                        Text("🏆")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Selection section
            if (!showQuestions) {
                item {
                    SectionHeaderCard("Kategorie")
                }
                item {
                    SelectionSection(
                        options = categoriesList,
                        selectedOption = selectedCategory,
                        onSelect = { selectedCategory = it }
                    )
                }
                item {
                    SectionHeaderCard("Schwierigkeit")
                }
                item {
                    SelectionSection(
                        options = difficultyList,
                        selectedOption = selectedDifficulty,
                        onSelect = { selectedDifficulty = it }
                    )
                }
                item {
                    SectionHeaderCard("Fragentyp")
                }
                item {
                    SelectionSection(
                        options = typesList,
                        selectedOption = selectedType,
                        onSelect = { selectedType = it }
                    )
                }

                if (showLoadButton) {
                    item(key = loadButtonIndex) {
                        val categoryId = mapCategoryToId(
                            selectedCategory
                        )
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Button(
                                onClick = {
                                    viewModel.fetchQuestions(
                                        amount = 5,
                                        category = categoryId,
                                        difficulty = selectedDifficulty,
                                        type = selectedType
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(0.6f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF458B74),
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Fragen laden")
                            }
                        }
                    }
                }
            }

            // Fragen Liste
            if (showQuestions) {
                items(
                    items = uiState.questions,
                    key = { it.question }
                ) { question ->
                    QuestionItem(
                        question = question,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = {
                                lastScore = viewModel.evaluateScore()
                                viewModel.evaluateScoreAndSave()
                                showResultDialog = true
                            },
                            modifier = Modifier.fillMaxWidth(0.6f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF458B74),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Auswerten")
                        }
                    }
                }
            }

            // Ergebnis Dialog
            if (showResultDialog) {
                item {
                    AlertDialog(
                        onDismissRequest = { },
                        confirmButton = {},
                        title = {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Ergebnis")
                                TextButton(onClick = {
                                    showResultDialog = false
                                    viewModel.resetQuiz()
                                    selectedCategory = null
                                    selectedDifficulty = null
                                    selectedType = null
                                }) {
                                    Text("X")
                                }
                            }
                        },
                        text = {
                            Text("Du hast $lastScore von ${uiState.questions.size} richtig!")
                        }
                    )
                }
            }
        }
    }
}

val categoriesList = listOf(
    "General Knowledge",
    "Entertainment: Film",
    "Entertainment: Music",
    "Entertainment: Musicals & Theatres",
    "Entertainment: Television",
    "Science & Nature",
    "Science: Computers",
    "Sports",
    "Animals",
    "Celebrities"
)

val difficultyList = listOf("easy", "medium", "hard")
val typesList = listOf("multiple", "boolean")
fun mapCategoryToId(category: String?): Int = when (category) {
    "General Knowledge" -> 9
    "Entertainment: Film" -> 11
    "Entertainment: Music" -> 12
    "Entertainment: Musicals & Theatres" -> 13
    "Entertainment: Television" -> 14
    "Science & Nature" -> 17
    "Science: Computers" -> 18
    "Sports" -> 21
    "Animals" -> 27
    "Celebrities" -> 26
    else -> 0
}

