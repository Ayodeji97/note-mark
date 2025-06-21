package com.danzucker.notemark.auth.domain

class UserDataValidator(
    private val patternValidator: PatternValidator
) {

    fun validateUsername(username: String): UsernameValidationState {
        val hasLessThanThreeCharacters = username.length < 3
        val hasMoreThanTwentyCharacters = username.length > 20
        val hasValidCharacters = username.isNotBlank() && username.length in 3..20

        return UsernameValidationState(
            hasLessThanThreeCharacters = hasLessThanThreeCharacters,
            hasMoreThanTwentyCharacters = hasMoreThanTwentyCharacters,
            hasValidCharacters = hasValidCharacters
        )
    }

    fun isValidEmail(email: String): Boolean {
        return patternValidator.matches(email.trim())
    }

    fun validatePassword(password: String, confirmPassword: String = ""): PasswordValidationState {
        val hasMinLength = password.length >= MIN_PASSWORD_LENGTH
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.contains(Regex("[^A-Za-z0-9]"))
        val hasValidConfirmPassword = password == confirmPassword

        return PasswordValidationState(
            hasMinLength = hasMinLength,
            hasNumber = hasDigit,
            hasSpecialCharacter = hasSpecialChar,
            hasValidConfirmPassword = hasValidConfirmPassword
        )
    }

    companion object {
        const val MIN_PASSWORD_LENGTH = 8
    }
}