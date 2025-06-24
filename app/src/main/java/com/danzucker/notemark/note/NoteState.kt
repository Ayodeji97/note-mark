package com.danzucker.notemark.note

data class NoteState(
    val paramOne: String = "default",
    val paramTwo: List<String> = emptyList(),
)