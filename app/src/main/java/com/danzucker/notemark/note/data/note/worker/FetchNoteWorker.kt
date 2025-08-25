package com.danzucker.notemark.note.data.note.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.danzucker.notemark.core.domain.util.toWorkerResult
import com.danzucker.notemark.note.domain.note.NoteRepository
import com.danzucker.notemark.core.domain.util.Result as NoteResult

class FetchNoteWorker(
    context: Context,
    private val params: WorkerParameters,
    private val noteRepository: NoteRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }

        return when (val result = noteRepository.fetchNotes()) {
            is NoteResult.Success -> Result.success()
            is NoteResult.Error -> {
                result.error.toWorkerResult()
            }
        }
    }


    companion object {
        const val NOTE_ID = "NOTE_ID"
        const val FETCH_NOTE_SYNC_WORKER_NAME = "fetch_note_sync_worker"
    }
}