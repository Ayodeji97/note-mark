package com.danzucker.notemark.auth.presentation.register

import com.danzucker.notemark.auth.domain.PasswordValidationState
import com.danzucker.notemark.auth.domain.UsernameValidationState

data class RegisterState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isEmailValid: Boolean = false,
    val usernameValidationState: UsernameValidationState = UsernameValidationState(),
    val passwordValidationState: PasswordValidationState = PasswordValidationState(),
    val canRegister: Boolean = false,
    val isRegistering: Boolean = false
)