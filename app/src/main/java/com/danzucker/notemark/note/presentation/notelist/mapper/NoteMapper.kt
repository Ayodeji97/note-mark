package com.danzucker.notemark.note.presentation.notelist.mapper

import com.danzucker.notemark.note.domain.note.model.Note
import com.danzucker.notemark.note.models.NoteUi
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun Note.toNoteUi(): NoteUi {
    return NoteUi(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt.toString(),
        lastEditAt = lastEditAt.toString()
    )
}