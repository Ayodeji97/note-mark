package com.danzucker.notemark.note.domain.note.local

import com.danzucker.notemark.core.domain.util.DataError
import com.danzucker.notemark.core.domain.util.Result
import com.danzucker.notemark.note.domain.note.model.Note
import com.danzucker.notemark.note.domain.note.model.Notes
import kotlinx.coroutines.flow.Flow

typealias NoteId = String

interface LocalNoteDataSource {
    fun getNotes(): Flow<List<Note>>
    suspend fun upsertNote(note: Note): Result<NoteId, DataError.Local>
    suspend fun upsertNotes(notes: Notes): Result<List<NoteId>, DataError.Local>
    suspend fun deleteNote(id: String)
    suspend fun deleteAllNotes()
}