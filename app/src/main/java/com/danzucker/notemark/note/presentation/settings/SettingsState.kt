package com.danzucker.notemark.note.presentation.settings

import com.danzucker.notemark.R
import com.danzucker.notemark.core.presentation.util.UiText
import com.danzucker.notemark.note.presentation.settings.util.SyncIntervalUi

data class SettingsState(
    val syncIntervalText: UiText = UiText.StringResourceWithArgs(R.string.manual_only),
    val selectedSyncInterval: SyncIntervalUi = SyncIntervalUi.MANUAL,
    val showSyncIntervalDropdown: Boolean = false,
)