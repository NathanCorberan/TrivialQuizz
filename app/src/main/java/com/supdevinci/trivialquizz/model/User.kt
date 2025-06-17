package com.supdevinci.trivialquizz.model

/**
 * Represents a user in the application.
 * This class follows the immutable data pattern to prevent unexpected state changes.
 *
 * @property firstname The user's first name
 * @property lastname The user's last name
 */
data class User(
    val firstname: String,
    val lastname: String
) {
    /**
     * Creates a copy of this user with a new first name.
     *
     * @param newFirstname The new first name
     * @return A new User instance with the updated first name
     */
    fun withFirstname(newFirstname: String): User = copy(firstname = newFirstname)

    /**
     * Creates a copy of this user with a new last name.
     *
     * @param newLastname The new last name
     * @return A new User instance with the updated last name
     */
    fun withLastname(newLastname: String): User = copy(lastname = newLastname)
}
