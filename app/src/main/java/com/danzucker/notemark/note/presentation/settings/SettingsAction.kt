package com.danzucker.notemark.note.presentation.settings

import com.danzucker.notemark.note.presentation.settings.util.SyncIntervalUi

sealed interface SettingsAction {
    data object OnBackClick : SettingsAction
    data object OnLogoutClick : SettingsAction
    data object OnSyncIntervalClick : SettingsAction
    data class OnSyncIntervalItemSelected(val syncInterval: SyncIntervalUi) : SettingsAction
    data object OnDismissSyncIntervalDropdown : SettingsAction
    data object OnSyncDataClick : SettingsAction // This action is used to trigger a manual sync of data
    data object OnConfirmLogout : SettingsAction
    data object OnCancelLogout : SettingsAction
    data object OnSyncAndLogout : SettingsAction
}