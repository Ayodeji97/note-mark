package com.danzucker.notemark.note.presentation.notelist

sealed interface NoteEvent {
    data class OnCreateNoteClick(
        val noteId: String? = null
    ): NoteEvent
}