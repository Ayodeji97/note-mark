@file:OptIn(ExperimentalTime::class)

package com.danzucker.notemark.note.data.note.mappers

import com.danzucker.notemark.core.database.entity.NoteEntity
import com.danzucker.notemark.note.data.note.network.NoteDto
import com.danzucker.notemark.note.data.note.network.NotesResponse
import com.danzucker.notemark.note.domain.note.model.Note
import com.danzucker.notemark.note.domain.note.model.NoteSaveStatus
import com.danzucker.notemark.note.domain.note.model.Notes
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun NoteEntity.toNote(): Note {
    return Note(
        id = id,
        title = title,
        content = content,
        createdAt = Instant.parse(createdAt),
        lastEditedAt = Instant.parse(lastEditAt),
        saveStatus = NoteSaveStatus.valueOf(saveStatus) // Convert string to enum
    )
}

fun Note.toNoteEntity(): NoteEntity {
    return NoteEntity(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt.toString(),
        lastEditAt = lastEditedAt.toString(),
        saveStatus = saveStatus.name // Convert enum to string
    )
}

fun NoteDto.toNote(): Note {
    return Note(
        id = id,
        title = title,
        content = content,
        createdAt = Instant.parse(createdAt),
        lastEditedAt = lastEditAt?.let { Instant.parse(it) } ?: Instant.parse(createdAt)
    )
}

fun Note.toNoteDto(): NoteDto {
    return NoteDto(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt.toString(),
        lastEditAt = lastEditedAt.toString()
    )
}

fun NotesResponse.toNotes(): Notes {
    return Notes(
        notes = notes.map { it.toNote() },
        total = total
    )
}

fun Notes.toNotesResponse(): NotesResponse {
    return NotesResponse(
        notes = notes.map { it.toNoteDto() },
        total = total
    )
}

fun List<NoteEntity>.toNotes(): List<Note> {
    return map { it.toNote() }
}


fun List<Note>.toNoteEntities(): List<NoteEntity> {
    return map { it.toNoteEntity() }
}

fun List<Note>.toNoteDtos(): List<NoteDto> {
    return map { it.toNoteDto() }
}