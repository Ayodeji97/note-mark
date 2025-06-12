package com.danzucker.notemark.auth.presentation.landing

sealed interface LandingAction {
    data object OnLoginClick: LandingAction
    data object OnGetStartedClick: LandingAction
}