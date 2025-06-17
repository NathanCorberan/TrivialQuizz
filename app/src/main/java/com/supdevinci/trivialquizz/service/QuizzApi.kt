package com.supdevinci.trivialquizz.service

import com.supdevinci.trivialquizz.model.QuestionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * DEPRECATED: This interface is deprecated and has been replaced by TriviaApiService.
 * It is kept for backward compatibility but should not be used in new code.
 * Use TriviaApiService instead.
 */
@Deprecated("Use TriviaApiService instead", ReplaceWith("TriviaApiService"))
interface QuizzApi {
    /**
     * Gets a specified number of trivia questions from the API.
     *
     * @param amount The number of questions to retrieve (default: 1)
     * @return A Response containing the question data
     */
    @GET("api.php")
    suspend fun getQuestion(@Query("amount") amount: Int = 1): Response<QuestionResponse>
}
