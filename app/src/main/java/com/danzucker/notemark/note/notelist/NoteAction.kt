package com.danzucker.notemark.note.notelist

sealed interface NoteAction {
    data object OnCreateNoteClick : NoteAction
    data object OnProfileClick : NoteAction
}