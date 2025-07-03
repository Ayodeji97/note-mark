package com.danzucker.notemark.note.presentation.notelist

import com.danzucker.notemark.note.models.NoteUi

data class NoteState(
    val currentNoteId: String? = null,
    val notes: List<NoteUi> = emptyList(),
    val showConfirmationDialog: Boolean = false,
    val isLoadingData: Boolean = true,
    val hasNotes: Boolean = false,
    val userProfileInitials: String = "",
)