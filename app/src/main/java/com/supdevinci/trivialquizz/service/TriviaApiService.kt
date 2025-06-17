package com.supdevinci.trivialquizz.service

import com.supdevinci.trivialquizz.model.CategoryResponse
import com.supdevinci.trivialquizz.model.QuestionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for the Open Trivia Database API.
 * This interface defines endpoints for retrieving trivia categories and questions.
 */
interface TriviaApiService {
    /**
     * Gets all available trivia categories from the API.
     *
     * @return A Response containing the categories data
     */
    @GET("api_category.php")
    suspend fun getCategories(): Response<CategoryResponse>

    /**
     * Gets a specified number of trivia questions from the API with optional filters.
     *
     * @param amount The number of questions to retrieve (default: 10)
     * @param type The type of questions to retrieve (default: "multiple")
     * @param category The category ID to filter questions by (optional)
     * @param difficulty The difficulty level to filter questions by (optional)
     * @return A Response containing the question data
     */
    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") amount: Int = 10,
        @Query("type") type: String = "multiple",
        @Query("category") category: Int? = null,
        @Query("difficulty") difficulty: String? = null
    ): Response<QuestionResponse>

    /**
     * Gets a single trivia question from the API.
     * This is a convenience method for getting a single question.
     *
     * @param amount The number of questions to retrieve (default: 1)
     * @return A Response containing the question data
     */
    @GET("api.php")
    suspend fun getQuestion(@Query("amount") amount: Int = 1): Response<QuestionResponse>
}
