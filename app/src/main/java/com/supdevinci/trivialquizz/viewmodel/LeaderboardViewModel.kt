package com.supdevinci.trivialquizz.viewmodel

import androidx.lifecycle.ViewModel
import com.supdevinci.trivialquizz.model.ScoreEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * ViewModel for managing leaderboard scores.
 * This class follows the MVVM pattern and uses StateFlow for state management.
 */
class LeaderboardViewModel : ViewModel() {

    // Scores state
    private val _scores = MutableStateFlow<List<ScoreEntry>>(emptyList())
    val scores: StateFlow<List<ScoreEntry>> = _scores

    // Date formatter for consistent date formatting
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

    /**
     * Adds a new score entry to the leaderboard.
     *
     * @param playerName The name of the player
     * @param categoryName The category of the quiz
     * @param difficulty The difficulty of the quiz
     * @param correctAnswers The number of correct answers
     * @param totalQuestions The total number of questions
     */
    fun addScore(
        playerName: String,
        categoryName: String,
        difficulty: String,
        correctAnswers: Int,
        totalQuestions: Int
    ) {
        val currentScores = _scores.value.toMutableList()
        val percentage = if (totalQuestions > 0) (correctAnswers * 100) / totalQuestions else 0
        val scoreText = "$correctAnswers/$totalQuestions"
        val currentDate = dateFormatter.format(Date())

        // Calculate rank (1-based)
        val rank = currentScores.size + 1

        val newScore = ScoreEntry(
            rank = rank,
            player = playerName,
            category = categoryName,
            difficulty = difficulty,
            score = scoreText,
            percentage = percentage,
            date = currentDate
        )

        currentScores.add(newScore)

        // Sort by percentage (descending) and update ranks
        val sortedScores = currentScores.sortedByDescending { it.percentage }
            .mapIndexed { index, entry -> 
                entry.copy(rank = index + 1) 
            }

        _scores.value = sortedScores
    }

    /**
     * Clears all scores from the leaderboard.
     */
    fun clearScores() {
        _scores.value = emptyList()
    }

    /**
     * Filters scores by category.
     *
     * @param category The category to filter by, or null for all categories
     * @return A list of filtered scores
     */
    fun filterByCategory(category: String?): List<ScoreEntry> {
        return if (category == null) {
            _scores.value
        } else {
            _scores.value.filter { it.category == category }
        }
    }

    /**
     * Filters scores by difficulty.
     *
     * @param difficulty The difficulty to filter by, or null for all difficulties
     * @return A list of filtered scores
     */
    fun filterByDifficulty(difficulty: String?): List<ScoreEntry> {
        return if (difficulty == null) {
            _scores.value
        } else {
            _scores.value.filter { it.difficulty == difficulty }
        }
    }
}
