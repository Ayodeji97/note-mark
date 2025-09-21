package com.danzucker.notemark.note.data.note.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.danzucker.notemark.core.database.dao.NotePendingSyncDao
import com.danzucker.notemark.core.domain.util.toWorkerResult
import com.danzucker.notemark.note.data.note.worker.CreateNoteWorker.Companion.NOTE_ID
import com.danzucker.notemark.note.domain.note.network.RemoteNoteDataSource
import com.danzucker.notemark.core.domain.util.Result as NoteResult

class DeleteNoteWorker(
    context: Context,
    private val params: WorkerParameters,
    private val remoteNoteDataSource: RemoteNoteDataSource,
    private val pendingSyncDao: NotePendingSyncDao
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }

        val noteId = params.inputData.getString(NOTE_ID) ?: return Result.failure()
        return when (val result = remoteNoteDataSource.deleteNote(noteId)) {
            is NoteResult.Success -> {
                pendingSyncDao.deleteDeletedNoteSyncEntity(noteId)
                Result.success()
            }
            is NoteResult.Error -> {
                result.error.toWorkerResult()
            }
        }
    }

    companion object {
        const val NOTE_ID = "NOTE_ID"
        const val DELETE_SYNC_WORK_NAME = "delete_note_sync_work"
    }

}