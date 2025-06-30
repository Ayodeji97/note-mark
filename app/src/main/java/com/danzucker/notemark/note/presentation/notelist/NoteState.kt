package com.danzucker.notemark.note.presentation.notelist

import com.danzucker.notemark.note.models.NoteUi

data class NoteState(
    val notes: List<NoteUi> = emptyList(),
)