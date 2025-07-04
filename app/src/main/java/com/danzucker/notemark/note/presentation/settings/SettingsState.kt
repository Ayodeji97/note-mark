package com.danzucker.notemark.note.presentation.settings

data class SettingsState(
    val paramOne: String = "default",
    val paramTwo: List<String> = emptyList(),
)