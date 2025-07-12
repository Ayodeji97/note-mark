package com.danzucker.notemark.note.presentation.notedetails

import com.danzucker.notemark.core.presentation.util.UiText
import com.danzucker.notemark.note.domain.note.model.NoteSaveStatus

data class NoteDetailsState(
    val id: String = "",
    val titleText: String = "",
    val contentText: String = "",
    val originalText: String = "",
    val originalContext: String = "",
    val showDiscardConfirmationDialog: Boolean = false,
    val isLoading: Boolean = false,
    val errorText: UiText? = null,
    val saveStatus: NoteSaveStatus = NoteSaveStatus.DRAFT,
    // This added state can be changes later during the note logic implementation
    val isViewMode: Boolean = true // For testing purposes, can be removed later
)