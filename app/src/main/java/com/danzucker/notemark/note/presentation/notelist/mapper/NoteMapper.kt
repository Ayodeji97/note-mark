@file:OptIn(ExperimentalTime::class)

package com.danzucker.notemark.note.presentation.notelist.mapper

import com.danzucker.notemark.note.domain.note.model.Note
import com.danzucker.notemark.note.models.NoteUi
import kotlin.time.ExperimentalTime

fun Note.toNoteUi(): NoteUi {
    return NoteUi(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt,
        lastEditAt = lastEditAt,
        saveStatus = saveStatus
    )
}

fun NoteUi.toNote(): Note {
    return Note(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt,
        lastEditAt = lastEditAt,
        saveStatus = saveStatus
    )
}