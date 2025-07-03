package com.danzucker.notemark.note.presentation.createnote

sealed interface CreateNoteAction {
    data class OnTitleTextChange(val text: String) : CreateNoteAction
    data class OnContentTextChange(val text: String) : CreateNoteAction
    data object OnCloseClick : CreateNoteAction
    data object OnSaveClick : CreateNoteAction
    data object OnKeepEditingClick : CreateNoteAction
    data object OnDiscardNoteClick : CreateNoteAction
    data object OnBacK : CreateNoteAction
}