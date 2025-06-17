package com.supdevinci.trivialquizz.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.supdevinci.trivialquizz.model.Category
import com.supdevinci.trivialquizz.ui.theme.TrivialQuizzTheme
import com.supdevinci.trivialquizz.viewmodel.QuizzViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        setContent {
            TrivialQuizzTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val quizzViewModel: QuizzViewModel = viewModel()
                    HomeScreen(
                        viewModel = quizzViewModel,
                        onStartQuiz = { playerName, category, difficulty ->
                            val intent = Intent(this, QuizActivity::class.java).apply {
                                putExtra("playerName", playerName)
                                putExtra("categoryId", category?.id ?: -1)
                                putExtra("categoryName", category?.name ?: "All Categories")
                                putExtra("difficulty", difficulty)
                                putExtra("difficultyDisplay", when(difficulty) {
                                    "easy" -> "Easy"
                                    "medium" -> "Medium"
                                    "hard" -> "Hard"
                                    else -> "Any"
                                })
                            }
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}