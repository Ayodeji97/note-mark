package com.danzucker.notemark.auth.presentation.register

import com.danzucker.notemark.core.presentation.util.UiText

sealed interface RegisterEvent {
    data object OnLoginTextClick : RegisterEvent
    data class Error(val error: UiText): RegisterEvent
    data object RegistrationSuccess : RegisterEvent
}