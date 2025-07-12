package com.danzucker.notemark.note.presentation.notedetails

sealed interface NoteDetailsEvent {
    data object FailedToSaveNoteDetails : NoteDetailsEvent
    data object NoteDetailsSuccessfullySaved : NoteDetailsEvent
    data object NavigateBack : NoteDetailsEvent
}