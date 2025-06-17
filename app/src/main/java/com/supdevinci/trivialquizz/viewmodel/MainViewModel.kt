package com.supdevinci.trivialquizz.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.supdevinci.trivialquizz.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing user information.
 * This class follows the MVVM pattern and uses StateFlow for state management.
 */
class MainViewModel : ViewModel() {

    // Using StateFlow for consistency with other ViewModels
    private val _user = MutableStateFlow(User("", ""))
    val user: StateFlow<User> = _user

    init {
        // Initialize with empty user instead of hardcoded values
        resetUser()
    }

    /**
     * Updates the user's first name.
     *
     * @param newFirstname The new first name
     */
    fun setFirstname(newFirstname: String) {
        _user.value = _user.value.withFirstname(newFirstname)
    }

    /**
     * Updates the user's last name.
     *
     * @param newLastname The new last name
     */
    fun setLastname(newLastname: String) {
        _user.value = _user.value.withLastname(newLastname)
    }

    /**
     * Resets the user to default values.
     * This is useful for clearing user data or initializing with default values.
     */
    fun resetUser() {
        _user.value = User("", "")
    }
}
