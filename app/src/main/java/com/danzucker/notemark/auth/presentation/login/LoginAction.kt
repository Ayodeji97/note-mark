package com.danzucker.notemark.auth.presentation.login

sealed interface LoginAction {
    data class OnEmailTextChange(val text: String): LoginAction
    data class OnPasswordTextChange(val text: String): LoginAction
    data object OnTogglePasswordVisibility : LoginAction
    data object OnLoginClick : LoginAction
    data object OnRegisterTextClick : LoginAction
}