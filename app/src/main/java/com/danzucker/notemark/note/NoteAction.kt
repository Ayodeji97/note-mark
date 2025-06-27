package com.danzucker.notemark.note

sealed interface NoteAction {
    data object OnCreateNoteClick : NoteAction
    data object OnProfileClick : NoteAction
}