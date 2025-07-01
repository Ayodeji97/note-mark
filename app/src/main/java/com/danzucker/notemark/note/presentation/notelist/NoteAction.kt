package com.danzucker.notemark.note.presentation.notelist

sealed interface NoteAction {
    data object OnCreateNoteClick : NoteAction
    data object OnProfileClick : NoteAction
}