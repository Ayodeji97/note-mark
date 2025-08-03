package com.danzucker.notemark.note.presentation.settings

import com.danzucker.notemark.note.presentation.settings.util.SyncIntervalUi

sealed interface SettingsAction {
    data object OnBackClick : SettingsAction
    data object OnLogoutClick : SettingsAction
    data object OnSyncIntervalClick : SettingsAction
    data class OnSyncIntervalItemSelected(val syncInterval: SyncIntervalUi) : SettingsAction
    data object OnDismissSyncIntervalDropdown : SettingsAction
}