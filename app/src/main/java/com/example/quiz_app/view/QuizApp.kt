package com.example.quiz_app.view

import HighscoreScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quiz_app.viewmodel.QuizViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

sealed class ScreenRoute(val route: String)

object MainRoute : ScreenRoute("main")
object HighscoreRoute : ScreenRoute("highscore/{date}") {
    fun createRoute(date: String) = "highscore/$date"
}

@Preview
@Composable
private fun AppPreview() {
    QuizApp()
}

@Composable
fun QuizApp(
    modifier: Modifier = Modifier,
    quizViewModel: QuizViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()



    NavHost(
        navController = navController,
        startDestination = MainRoute.route,
        modifier = modifier
    ) {
        composable(MainRoute.route) {
            QuizScreen(
                viewModel = quizViewModel,
                onNavigatetoHighscore = {
                    val date = LocalDate.now()
                        .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))

                    navController.navigate(
                        HighscoreRoute.createRoute(date)
                    )
                }
            )
        }

        composable(
            route = HighscoreRoute.route,
            arguments = listOf(
                navArgument("date") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val date = backStackEntry.arguments?.getString("date") ?: "—"

            HighscoreScreen(
                viewModel = quizViewModel,
                date = date,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
    }