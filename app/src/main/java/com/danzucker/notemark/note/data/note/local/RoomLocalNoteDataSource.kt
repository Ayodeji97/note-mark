package com.danzucker.notemark.note.data.note.local

import android.database.sqlite.SQLiteFullException
import com.danzucker.notemark.core.database.dao.NoteDao
import com.danzucker.notemark.core.domain.util.DataError
import com.danzucker.notemark.core.domain.util.Result
import com.danzucker.notemark.note.data.note.mappers.toNoteEntity
import com.danzucker.notemark.note.data.note.mappers.toNotes
import com.danzucker.notemark.note.domain.note.local.LocalNoteDataSource
import com.danzucker.notemark.note.domain.note.model.Note
import com.danzucker.notemark.note.domain.note.model.Notes
import com.danzucker.notemark.note.domain.note.local.NoteId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomLocalNoteDataSource(
    private val noteDao: NoteDao
) : LocalNoteDataSource {

    override fun getNotes(): Flow<List<Note>> {
        return noteDao.getNotes().map { noteEntities ->
            noteEntities.toNotes()
        }
    }

    override suspend fun upsertNote(note: Note): Result<NoteId, DataError.Local> {
        return try {
            val entity = note.toNoteEntity()
            noteDao.upsertNote(entity)
            Result.Success(entity.id)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun upsertNotes(notes: Notes): Result<List<NoteId>, DataError.Local> {
        return try {
            val entities = notes.notes.map { it.toNoteEntity() }
            noteDao.upsertNotes(entities)
            Result.Success(entities.map { it.id })
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteNote(id: String) {
        noteDao.deleteNoteById(id)
    }

    override suspend fun deleteAllNotes() {
        noteDao.deleteAllNotes()
    }
}