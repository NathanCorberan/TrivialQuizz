package com.supdevinci.trivialquizz.model

import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents a trivia question with its details.
 *
 * @property category The category of the question
 * @property type The type of question (e.g., multiple choice)
 * @property difficulty The difficulty level of the question
 * @property question The question text
 * @property correctAnswer The correct answer to the question
 * @property incorrectAnswers List of incorrect answers
 */
@Parcelize
data class Question(
    @SerializedName("category")
    val category: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("difficulty")
    val difficulty: String,
    @SerializedName("question")
    val question: String,
    @SerializedName("correct_answer")
    val correctAnswer: String,
    @SerializedName("incorrect_answers")
    val incorrectAnswers: List<String>
) : Parcelable

/**
 * Represents the response from the trivia API containing questions.
 *
 * @property responseCode The response code from the API
 * @property results List of questions returned by the API
 */
data class QuestionResponse(
    @SerializedName("response_code")
    val responseCode: Int,
    @SerializedName("results")
    val results: List<Question>
)

/**
 * Represents a user's answer to a question.
 *
 * @property question The question text
 * @property userAnswer The answer provided by the user
 * @property correctAnswer The correct answer to the question
 * @property isCorrect Whether the user's answer is correct
 */
@Parcelize
data class UserAnswer(
    val question: String,
    val userAnswer: String,
    val correctAnswer: String,
    val isCorrect: Boolean
) : Parcelable
