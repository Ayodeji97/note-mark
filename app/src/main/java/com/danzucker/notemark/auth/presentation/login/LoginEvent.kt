package com.danzucker.notemark.auth.presentation.login

import com.danzucker.notemark.core.presentation.util.UiText

sealed interface LoginEvent {
    data object OnRegisterTextClick: LoginEvent
    data object LoginSuccess: LoginEvent
    data class Error(val error: UiText): LoginEvent
}