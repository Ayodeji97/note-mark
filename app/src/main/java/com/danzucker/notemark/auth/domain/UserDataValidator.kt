package com.danzucker.notemark.auth.domain

class UserDataValidator(
    private val patternValidator: PatternValidator
) {

    fun isValidEmail(email: String): Boolean {
        return patternValidator.matches(email.trim())
    }

    fun validatePassword(password: String): PasswordValidationState {
        val hasMinLength = password.length >= MIN_PASSWORD_LENGTH
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.contains(Regex("[^A-Za-z0-9]"))

        return PasswordValidationState(
            hasMinLength = hasMinLength,
            hasNumber = hasDigit,
            hasSpecialCharacter = hasSpecialChar,
        )
    }

    fun validateConfirmPassword(password: String, confirmPassword: String): PasswordValidationState {
        return PasswordValidationState(
            hasValidConfirmPassword = password == confirmPassword
        )
    }

    companion object {
        const val MIN_PASSWORD_LENGTH = 8
    }
}