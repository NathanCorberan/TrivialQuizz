package com.supdevinci.trivialquizz.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.supdevinci.trivialquizz.data.local.ScoreDatabase
import com.supdevinci.trivialquizz.data.local.ScoreEntity
import com.supdevinci.trivialquizz.data.local.ScoreRepository
import com.supdevinci.trivialquizz.model.ScoreEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * ViewModel for managing leaderboard scores.
 * This class follows the MVVM pattern and uses StateFlow for state management.
 */
class LeaderboardViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ScoreRepository

    // Scores state
    private val _scores = MutableStateFlow<List<ScoreEntry>>(emptyList())
    val scores: StateFlow<List<ScoreEntry>> = _scores

    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

    init {
        val dao = ScoreDatabase.getDatabase(application).scoreDao()
        repository = ScoreRepository(dao)
        viewModelScope.launch {
            repository.allScores.collectLatest { list ->
                val entries = list
                    .sortedByDescending { it.percentage }
                    .mapIndexed { index, entity ->
                        ScoreEntry(
                            rank = index + 1,
                            player = entity.player,
                            category = entity.category,
                            difficulty = entity.difficulty,
                            score = entity.score,
                            percentage = entity.percentage,
                            date = dateFormatter.format(entity.date)
                        )
                    }
                _scores.value = entries
            }
        }
    }

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
        val percentage = if (totalQuestions > 0) (correctAnswers * 100) / totalQuestions else 0
        val scoreText = "$correctAnswers/$totalQuestions"
        val entity = ScoreEntity(
            player = playerName,
            category = categoryName,
            difficulty = difficulty,
            score = scoreText,
            percentage = percentage,
            date = Date()
        )
        viewModelScope.launch {
            repository.insert(entity)
        }
    }

    /**
     * Clears all scores from the leaderboard.
     */
    fun clearScores() {
        viewModelScope.launch {
            repository.clear()
        }
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
