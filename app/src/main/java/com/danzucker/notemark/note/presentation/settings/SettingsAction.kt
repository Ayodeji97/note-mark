package com.danzucker.notemark.note.presentation.settings

sealed interface SettingsAction {
    data object OnBackClick : SettingsAction
    data object OnLogoutClick : SettingsAction
}