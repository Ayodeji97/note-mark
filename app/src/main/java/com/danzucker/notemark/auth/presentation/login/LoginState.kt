package com.danzucker.notemark.auth.presentation.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val canLogin: Boolean = false,
    val isLoggingIn: Boolean = false
)