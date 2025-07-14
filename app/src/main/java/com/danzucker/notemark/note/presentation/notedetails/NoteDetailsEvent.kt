package com.danzucker.notemark.note.presentation.notedetails

sealed interface NoteDetailsEvent {
    data object FailedToSaveNoteDetails : NoteDetailsEvent
    data object NoteDetailsSuccessfullySaved : NoteDetailsEvent
    data object NavigateBack : NoteDetailsEvent
    data object RequestLandscapeOrientation : NoteDetailsEvent
    data object ResetOrientation : NoteDetailsEvent
}