package com.supdevinci.trivialquizz.data

import com.supdevinci.trivialquizz.model.Category
import com.supdevinci.trivialquizz.model.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository class for handling data operations related to trivia questions and categories.
 * This class follows the repository pattern to separate data sources from the rest of the application.
 */
class TriviaRepository {
    private val apiService = RetrofitInstance.triviaApiService

    /**
     * Gets all available trivia categories from the API.
     *
     * @return A list of categories, or an empty list if the request fails
     */
    suspend fun getCategories(): List<Category> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getCategories()
            if (response.isSuccessful) {
                response.body()?.categories ?: emptyList()
            } else {
                // Log error or handle specific error codes if needed
                emptyList()
            }
        } catch (e: Exception) {
            // Log exception
            emptyList()
        }
    }

    /**
     * Gets trivia questions from the API with optional filters.
     *
     * @param category The category ID to filter questions by (optional)
     * @param difficulty The difficulty level to filter questions by (optional)
     * @return A list of questions, or an empty list if the request fails
     */
    suspend fun getQuestions(
        category: Int? = null,
        difficulty: String? = null
    ): List<Question> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getQuestions(
                category = category,
                difficulty = difficulty
            )

            if (response.isSuccessful && response.body()?.responseCode == 0) {
                response.body()?.results ?: emptyList()
            } else {
                // Fallback to general questions if specific query fails
                try {
                    val fallbackResponse = apiService.getQuestions()
                    if (fallbackResponse.isSuccessful) {
                        fallbackResponse.body()?.results ?: emptyList()
                    } else {
                        emptyList()
                    }
                } catch (e: Exception) {
                    // Log exception
                    emptyList()
                }
            }
        } catch (e: Exception) {
            // Log exception
            emptyList()
        }
    }
}
