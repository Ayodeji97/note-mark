package com.danzucker.notemark.note.presentation.createnote

sealed interface CreateNoteEvent {
    data object NoteSuccessfullySaved : CreateNoteEvent
}