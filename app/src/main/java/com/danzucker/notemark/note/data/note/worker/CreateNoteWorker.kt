package com.danzucker.notemark.note.data.note.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.danzucker.notemark.core.database.dao.NotePendingSyncDao
import com.danzucker.notemark.core.domain.util.toWorkerResult
import com.danzucker.notemark.note.data.note.mappers.toNote
import com.danzucker.notemark.note.domain.note.network.RemoteNoteDataSource
import com.danzucker.notemark.core.domain.util.Result as NoteResult

class CreateNoteWorker(
    context: Context,
    private val params: WorkerParameters,
    private val remoteNoteDataSource: RemoteNoteDataSource,
    private val pendingSyncDao: NotePendingSyncDao
) : CoroutineWorker(context, params)  {

    override suspend fun doWork(): Result {
            if (runAttemptCount >= 5) {
                return Result.failure()
            }

        val pendingNoteId = params.inputData.getString(NOTE_ID) ?: return Result.failure()
        val pendingNoteEntity = pendingSyncDao.getNotePendingSyncEntity(pendingNoteId)
            ?: return Result.failure()

        val note = pendingNoteEntity.note.toNote()

        return when (val result = remoteNoteDataSource.postNote(note)) {
            is NoteResult.Success -> {
                pendingSyncDao.deleteNotePendingSyncEntity(pendingNoteId)
                Result.success()
            }
            is NoteResult.Error -> {
                result.error.toWorkerResult()
            }
        }
    }

    companion object {
        const val NOTE_ID = "NOTE_ID"
        const val SYNC_WORK_NAME = "create_note_sync_work"
    }
}