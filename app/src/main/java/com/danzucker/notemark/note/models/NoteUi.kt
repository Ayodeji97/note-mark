package com.danzucker.notemark.note.models

data class NoteUi(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: String,
    val lastEditAt: String
)
