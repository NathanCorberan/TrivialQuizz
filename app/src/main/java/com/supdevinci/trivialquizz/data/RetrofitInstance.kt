package com.supdevinci.trivialquizz.data

import com.supdevinci.trivialquizz.service.TriviaApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton object that provides a Retrofit instance for making API calls.
 * This class follows the singleton pattern to ensure only one instance of Retrofit is created.
 */
object RetrofitInstance {
    private const val BASE_URL = "https://opentdb.com/"

    /**
     * Retrofit instance configured with the base URL and Gson converter.
     */
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * TriviaApiService instance for making API calls.
     * This is a convenience property to avoid creating the service in multiple places.
     */
    val triviaApiService: TriviaApiService by lazy {
        retrofit.create(TriviaApiService::class.java)
    }
}
