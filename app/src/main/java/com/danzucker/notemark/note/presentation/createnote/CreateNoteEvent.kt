package com.danzucker.notemark.note.presentation.createnote

sealed interface CreateNoteEvent {
    data object FailedToSaveNote : CreateNoteEvent
    data object NoteSuccessfullySaved : CreateNoteEvent
    data object NavigateBack : CreateNoteEvent
}