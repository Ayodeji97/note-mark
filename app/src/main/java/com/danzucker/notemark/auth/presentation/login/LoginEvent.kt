package com.danzucker.notemark.auth.presentation.login

sealed interface LoginEvent {
    data object OnRegisterTextClick: LoginEvent
}