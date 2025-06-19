package com.danzucker.notemark.auth.presentation.register

import com.danzucker.notemark.auth.presentation.login.LoginAction

sealed interface RegisterAction {
    data class OnUsernameTextChange(val text: String): RegisterAction
    data class OnEmailTextChange(val text: String): RegisterAction
    data class OnPasswordTextChange(val text: String): RegisterAction
    data class OnConfirmPasswordTextChange(val text: String): RegisterAction
    data object OnRegisterClick: RegisterAction
    data object OnLoginTextClick: RegisterAction
}