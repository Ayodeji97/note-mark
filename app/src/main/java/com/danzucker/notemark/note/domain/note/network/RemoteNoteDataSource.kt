package com.danzucker.notemark.note.domain.note.network

import com.danzucker.notemark.core.domain.util.DataError
import com.danzucker.notemark.core.domain.util.Result
import com.danzucker.notemark.note.domain.note.model.Note
import com.danzucker.notemark.note.domain.note.model.Notes

interface RemoteNoteDataSource {
    suspend fun getNotes(): Result<Notes, DataError.Network>
    suspend fun postNote(note: Note): Result<Note, DataError.Network>
    suspend fun updateNote(note: Note): Result<Note, DataError.Network>
    suspend fun deleteNote(id: String): Result<Unit, DataError.Network>
}