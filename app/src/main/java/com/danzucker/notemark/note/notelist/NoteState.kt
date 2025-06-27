package com.danzucker.notemark.note.notelist

import com.danzucker.notemark.note.models.NoteUi

data class NoteState(
    val notes: List<NoteUi> = emptyList(),
)