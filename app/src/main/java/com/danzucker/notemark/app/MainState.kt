package com.danzucker.notemark.app

data class MainState(
    val isLoggedIn: Boolean = false,
    val isCheckingAuth: Boolean = false,
    val isAuthCheckComplete: Boolean = false,
    val showOnboarding: Boolean = false,
    val showSettings: Boolean = false
)