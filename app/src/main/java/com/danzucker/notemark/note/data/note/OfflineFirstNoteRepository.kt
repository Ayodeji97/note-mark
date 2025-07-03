package com.danzucker.notemark.note.data.note

import com.danzucker.notemark.core.domain.util.DataError
import com.danzucker.notemark.core.domain.util.EmptyResult
import com.danzucker.notemark.core.domain.util.Result
import com.danzucker.notemark.core.domain.util.asEmptyDataResult
import com.danzucker.notemark.note.domain.note.model.Note
import com.danzucker.notemark.note.domain.note.NoteRepository
import com.danzucker.notemark.note.domain.note.local.LocalNoteDataSource
import com.danzucker.notemark.note.domain.note.local.NoteId
import com.danzucker.notemark.note.domain.note.network.RemoteNoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow

class OfflineFirstNoteRepository(
    private val localNoteDataSource: LocalNoteDataSource,
    private val remoteNoteDataSource: RemoteNoteDataSource,
    private val applicationScope: CoroutineScope
) : NoteRepository {
    override fun getNotes(): Flow<List<Note>> {
        return localNoteDataSource.getNotes()
    }

    override suspend fun getNoteById(id: NoteId): Result<Note, DataError.Local> {
        return localNoteDataSource.getNoteById(id)
    }

    override suspend fun fetchNotes(): EmptyResult<DataError> {
        return when (val remoteNotesResult = remoteNoteDataSource.getNotes()) {
            is Result.Error -> remoteNotesResult.asEmptyDataResult()
            is Result.Success -> {
                applicationScope.async {
                    localNoteDataSource.upsertNotes(remoteNotesResult.data).asEmptyDataResult()
                }.await()
            }
        }
    }

    override suspend fun createNote(note: Note): EmptyResult<DataError> {
        val localResult = localNoteDataSource.upsertNote(note)

        if (localResult !is Result.Success) {
            return localResult.asEmptyDataResult()
        }

        return applicationScope.async {
            val remoteResult = remoteNoteDataSource.postNote(note)

            if (remoteResult is Result.Error) {
                return@async Result.Success(Unit)
            }
            return@async remoteResult.asEmptyDataResult()
        }.await()
    }

    override suspend fun deleteNote(id: NoteId) {
        localNoteDataSource.deleteNote(id)

        val remoteResult = applicationScope.async {
            remoteNoteDataSource.deleteNote(id)
        }.await()

        if (remoteResult is Result.Error) {
            // Handle error if needed, e.g., log it or retry
        }
    }

    override suspend fun deleteDraftNotes() {
        localNoteDataSource.deleteDraftNotes()
    }

    override suspend fun deleteAllNotes() {
        localNoteDataSource.deleteAllNotes()
    }
}