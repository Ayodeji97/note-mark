package com.danzucker.notemark.note.data.note.network

import kotlinx.serialization.Serializable

@Serializable
data class NotesResponse(
    val notes: List<NoteDto>,
    val total: Int,
)