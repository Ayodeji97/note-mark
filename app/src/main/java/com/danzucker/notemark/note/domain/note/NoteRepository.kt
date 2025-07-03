package com.danzucker.notemark.note.domain.note

import com.danzucker.notemark.core.domain.util.DataError
import com.danzucker.notemark.core.domain.util.EmptyResult
import com.danzucker.notemark.core.domain.util.Result
import com.danzucker.notemark.note.domain.note.local.NoteId
import com.danzucker.notemark.note.domain.note.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: NoteId): Result<Note, DataError.Local>
    suspend fun fetchNotes(): EmptyResult<DataError>
    suspend fun createNote(note: Note): EmptyResult<DataError>
    suspend fun deleteNote(id: NoteId)
    suspend fun deleteDraftNotes()
    suspend fun deleteAllNotes()
}