package com.danzucker.notemark.note

import com.danzucker.notemark.note.models.NoteUi

data class NoteState(
    val notes: List<NoteUi> = emptyList(),
)