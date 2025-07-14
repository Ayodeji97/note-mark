package com.danzucker.notemark.note.presentation.notedetails

sealed interface NoteDetailsAction {
    data class OnTitleTextChange(val text: String) : NoteDetailsAction
    data class OnContentTextChange(val text: String) : NoteDetailsAction
    data object OnCloseClick : NoteDetailsAction
    data object OnSaveClick : NoteDetailsAction
    data object OnKeepEditingClick : NoteDetailsAction
    data object OnDiscardNoteDetailsClick : NoteDetailsAction
    data object OnBacK : NoteDetailsAction
    data object OnViewModeClick : NoteDetailsAction
    data object OnEditModeClick : NoteDetailsAction
    data object OnReaderModeClick : NoteDetailsAction
    data object OnReaderScreenTap : NoteDetailsAction
    data object OnReaderScrollStart : NoteDetailsAction
}