package com.danzucker.notemark.auth.presentation.register

sealed interface RegisterEvent {
    data object OnLoginTextClick : RegisterEvent
}