package com.danzucker.notemark.note.presentation.settings

import com.danzucker.notemark.R
import com.danzucker.notemark.core.presentation.util.UiText
import com.danzucker.notemark.note.presentation.settings.util.SyncIntervalUi

data class SettingsState(
    val syncIntervalText: UiText = UiText.StringResourceWithArgs(R.string.manual_only),
    val selectedSyncInterval: SyncIntervalUi = SyncIntervalUi.MANUAL,
    val lastSyncTimestamp: UiText = UiText.StringResourceWithArgs(R.string.never_synced),
    val showSyncIntervalDropdown: Boolean = false,
    val isDeviceConnected: Boolean = false,
    val isSyncingData: Boolean = false,
    val showNoInternetMessage: UiText = UiText.StringResourceWithArgs(R.string.network_error_message),
    val showError: Boolean = false,
    val errorMessage: UiText = UiText.StringResourceWithArgs(R.string.error_message),
    val showLogoutConfirmationDialog: Boolean = false
)