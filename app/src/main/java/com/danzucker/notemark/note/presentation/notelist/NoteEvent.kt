package com.danzucker.notemark.note.presentation.notelist

sealed interface NoteEvent {
    data object OnCreateNoteClick: NoteEvent
}