package com.danzucker.notemark.note.presentation.notelist


sealed interface NoteAction {
    data object OnCreateNoteClick : NoteAction
    data object OnProfileClick : NoteAction
    data class OnDeleteNoteClick(val noteUiId: String) : NoteAction
    data object OnCancelClick : NoteAction
    data class OnNoteCardLongClick(val noteUiId: String) : NoteAction
    data class OnNoteCardClick(val noteUiId: String) : NoteAction
    data object OnDismissConfirmationDialog : NoteAction
    data object OnSettingsClick : NoteAction
}