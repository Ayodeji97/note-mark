package com.danzucker.notemark.note.presentation.createnote

import com.danzucker.notemark.core.presentation.util.UiText
import com.danzucker.notemark.note.domain.note.model.NoteSaveStatus

data class CreateNoteState(
    val id: String = "",
    val titleText: String = "",
    val contentText: String = "",
    val originalText: String = "",
    val originalContext: String = "",
    val showDiscardConfirmationDialog: Boolean = false,
    val isLoading: Boolean = false,
    val errorText: UiText? = null,
    val saveStatus: NoteSaveStatus = NoteSaveStatus.DRAFT,
)