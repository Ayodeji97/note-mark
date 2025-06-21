package com.danzucker.notemark.auth.domain

data class PasswordValidationState(
    val hasMinLength: Boolean = false,
    val hasNumber: Boolean = false,
    val hasSpecialCharacter: Boolean = false,
    val hasValidConfirmPassword: Boolean = false
) {
    val isValidPassword: Boolean
        get() = hasMinLength && (hasNumber || hasSpecialCharacter)
}