package com.supdevinci.trivialquizz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supdevinci.trivialquizz.data.TriviaRepository
import com.supdevinci.trivialquizz.model.Category
import com.supdevinci.trivialquizz.model.Question
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing trivia quiz data.
 * This class follows the MVVM pattern and uses StateFlow for state management.
 */
class QuizzViewModel : ViewModel() {
    // Use constructor injection for better testability
    private val repository = TriviaRepository()

    // Categories state
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    // Questions state
    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Error state
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // User selection states
    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> = _selectedCategory

    private val _selectedDifficulty = MutableStateFlow<String?>(null)
    val selectedDifficulty: StateFlow<String?> = _selectedDifficulty

    init {
        fetchCategories()
    }

    /**
     * Fetches all available trivia categories from the repository.
     * Updates the categories state and handles loading and error states.
     */
    fun fetchCategories() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val result = repository.getCategories()
                _categories.value = result

                if (result.isEmpty()) {
                    _error.value = "No categories found."
                }
            } catch (e: Exception) {
                _error.value = "Failed to load categories: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Sets the selected category.
     *
     * @param category The category to select, or null to clear the selection
     */
    fun selectCategory(category: Category?) {
        _selectedCategory.value = category
    }

    /**
     * Sets the selected difficulty.
     *
     * @param difficulty The difficulty to select, or null to clear the selection
     */
    fun selectDifficulty(difficulty: String?) {
        _selectedDifficulty.value = difficulty
    }

    /**
     * Fetches trivia questions based on the selected category and difficulty.
     * Updates the questions state and handles loading and error states.
     */
    fun fetchQuestions() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val catId = _selectedCategory.value?.id
                val diff = _selectedDifficulty.value

                val result = repository.getQuestions(category = catId, difficulty = diff)
                _questions.value = result

                if (result.isEmpty()) {
                    _error.value = "No questions found for the selected criteria."
                }
            } catch (e: Exception) {
                _error.value = "Failed to load questions: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Clears all selections and resets the ViewModel state.
     */
    fun reset() {
        _selectedCategory.value = null
        _selectedDifficulty.value = null
        _questions.value = emptyList()
        _error.value = null
    }
}
