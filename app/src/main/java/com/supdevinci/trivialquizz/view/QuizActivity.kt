package com.supdevinci.trivialquizz.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.supdevinci.trivialquizz.model.Category
import com.supdevinci.trivialquizz.model.ScoreEntry
import com.supdevinci.trivialquizz.model.UserAnswer
import com.supdevinci.trivialquizz.ui.theme.TrivialQuizzTheme
import com.supdevinci.trivialquizz.viewmodel.QuizzViewModel
import java.text.SimpleDateFormat
import java.util.*

class QuizActivity : ComponentActivity() {
    private val quizViewModel: QuizzViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val playerName = intent.getStringExtra("playerName") ?: "Anonymous"
        val categoryId = intent.getIntExtra("categoryId", -1)
        val categoryName = intent.getStringExtra("categoryName") ?: "All Categories"
        val difficulty = intent.getStringExtra("difficulty")
        val difficultyDisplay = intent.getStringExtra("difficultyDisplay") ?: "Any"
        
        // Set category and difficulty in ViewModel
        if (categoryId != -1) {
            quizViewModel.selectCategory(Category(categoryId, categoryName))
        }
        quizViewModel.selectDifficulty(difficulty)
        
        // Fetch questions
        quizViewModel.fetchQuestions()
        
        setContent {
            TrivialQuizzTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val questions by quizViewModel.questions.collectAsState()
                    var showResults by remember { mutableStateOf(false) }
                    var quizResults by remember { mutableStateOf(Triple(0, 0, emptyList<UserAnswer>())) }
                    
                    if (showResults) {
                        ResultScreen(
                            score = quizResults.first,
                            totalQuestions = quizResults.second,
                            answers = quizResults.third,
                            playerName = playerName,
                            categoryName = categoryName,
                            difficultyDisplay = difficultyDisplay,
                            onPlayAgain = {
                                finish()
                            }
                        )
                    } else {
                        if (questions.isNotEmpty()) {
                            QuizScreen(
                                questions = questions,
                                playerName = playerName,
                                categoryName = categoryName,
                                difficultyDisplay = difficultyDisplay,
                                onQuizFinished = { score, total, answers ->
                                    quizResults = Triple(score, total, answers)
                                    showResults = true
                                    
                                    // Save score to leaderboard
                                    saveScore(
                                        playerName = playerName,
                                        score = score,
                                        total = total,
                                        categoryName = categoryName,
                                        difficultyDisplay = difficultyDisplay
                                    )
                                }
                            )
                        } else {
                            LoadingScreen()
                        }
                    }
                }
            }
        }
    }
    
    private fun saveScore(
        playerName: String,
        score: Int,
        total: Int,
        categoryName: String,
        difficultyDisplay: String
    ) {
        // Calculate percentage
        val percentage = (score * 100) / total
        
        // Format current date and time
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val currentDateTime = sdf.format(Date())
        
        // Create score entry
        val scoreEntry = ScoreEntry(
            rank = 1, // This will be recalculated when displaying the leaderboard
            player = playerName,
            category = categoryName,
            difficulty = difficultyDisplay,
            score = "$score/$total",
            percentage = percentage,
            date = currentDateTime
        )
        
        // TODO: Save score to database or shared preferences
        // For now, we'll just pass it to LeaderboardActivity
        val intent = Intent(this, LeaderboardActivity::class.java)
        intent.putExtra("newScore", scoreEntry)
        // Don't start the activity yet, just prepare the intent
    }
}
