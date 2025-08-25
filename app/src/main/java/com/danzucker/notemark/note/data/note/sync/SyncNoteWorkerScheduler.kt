package com.danzucker.notemark.note.data.note.sync

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.await
import com.danzucker.notemark.core.database.dao.NotePendingSyncDao
import com.danzucker.notemark.core.database.entity.DeletedNoteSyncEntity
import com.danzucker.notemark.core.database.entity.NotePendingSyncEntity
import com.danzucker.notemark.core.domain.sessionstorage.SessionStorage
import com.danzucker.notemark.note.data.note.mappers.toNoteEntity
import com.danzucker.notemark.note.data.note.worker.CreateNoteWorker
import com.danzucker.notemark.note.data.note.worker.CreateNoteWorker.Companion.SYNC_WORK_NAME
import com.danzucker.notemark.note.data.note.worker.DeleteNoteWorker
import com.danzucker.notemark.note.data.note.worker.DeleteNoteWorker.Companion.DELETE_SYNC_WORK_NAME
import com.danzucker.notemark.note.data.note.worker.FetchNoteWorker
import com.danzucker.notemark.note.data.note.worker.FetchNoteWorker.Companion.FETCH_NOTE_SYNC_WORKER_NAME
import com.danzucker.notemark.note.domain.note.local.NoteId
import com.danzucker.notemark.note.domain.note.model.Note
import com.danzucker.notemark.note.domain.note.sync.SyncNoteScheduler
import com.danzucker.notemark.note.domain.note.sync.SyncType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.toJavaDuration


class SyncNoteWorkerScheduler(
    private val context: Context,
    private val pendingSyncDao: NotePendingSyncDao,
    private val sessionStorage: SessionStorage,
    private val applicationScope: CoroutineScope
) : SyncNoteScheduler {

    private val workManager = WorkManager.getInstance(context)

    override suspend fun scheduleSync(type: SyncType) {
        when (type) {
            is SyncType.FetchNotes -> scheduleFetchNotesWorker(interval = type.interval)
            is SyncType.CreateNote -> scheduleCreateNoteWorker(note = type.note)
            is SyncType.DeleteNote -> scheduleDeleteNoteWorker(noteId = type.noteId)
        }
    }

    private suspend fun scheduleCreateNoteWorker(
        note: Note
    ) {
        val username = sessionStorage.get()?.username ?: return

        val pendingNoteEntity = NotePendingSyncEntity(
            userName = username,
            note = note.toNoteEntity()
        )

        pendingSyncDao.upsertNotePendingSyncEntity(pendingNoteEntity)

        /**
         * Schedule a worker to create a note
         * This could involve creating a OneTimeWorkRequest, with the note data and adding it to the WorkManager
         */

        val createNoteConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<CreateNoteWorker>()
            .addTag(SYNC_WORK_NAME)
            .setConstraints(createNoteConstraints)
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 2000L, // 2 seconds
                timeUnit = TimeUnit.MILLISECONDS
            )
            .setInputData(
                Data.Builder()
                    .putString(CreateNoteWorker.NOTE_ID, pendingNoteEntity.noteId)
                    .build()
            )
            .build()

        applicationScope.launch {
            workManager.enqueue(workRequest).await()
        }.join()
    }

    private suspend fun scheduleDeleteNoteWorker(
        noteId: NoteId
    ) {
        val username = sessionStorage.get()?.username ?: return

        val pendingNoteEntity = DeletedNoteSyncEntity(
            noteId = noteId,
            username = username
        )

        pendingSyncDao.upsertDeletedNoteSyncEntity(pendingNoteEntity)


        /**
         * Schedule a worker to delete a note
         * This could involve creating a OneTimeWorkRequest, with the note data and adding it to the WorkManager
         */

        val deleteNoteConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<DeleteNoteWorker>()
            .addTag(DELETE_SYNC_WORK_NAME)
            .setConstraints(deleteNoteConstraints)
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 2000L, // 2 seconds
                timeUnit = TimeUnit.MILLISECONDS
            )
            .setInputData(
                Data.Builder()
                    .putString(DeleteNoteWorker.NOTE_ID, pendingNoteEntity.noteId)
                    .build()
            )
            .build()

        applicationScope.launch {
            workManager.enqueue(workRequest).await()
        }.join()
    }

    private suspend fun scheduleFetchNotesWorker(interval: Duration) {
       val isSyncWorkScheduled = withContext(Dispatchers.IO) {
           workManager
               .getWorkInfosByTag(FETCH_NOTE_SYNC_WORKER_NAME)
               .get()
               .isNotEmpty()
       }

        // We also want to check if the interval is already scheduled (store the last scheduled time)
        if (isSyncWorkScheduled) {
            // If a sync work is already scheduled, we want to cancel it and schedule a new one
            workManager
                .cancelAllWorkByTag(FETCH_NOTE_SYNC_WORKER_NAME)
            return
        }

        val fetchNotesConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<FetchNoteWorker>(
            repeatInterval = interval.toJavaDuration()
        ).addTag(FETCH_NOTE_SYNC_WORKER_NAME)
            .setConstraints(fetchNotesConstraints)
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = interval.inWholeMilliseconds,
                timeUnit = TimeUnit.MILLISECONDS
            )
            .setInitialDelay(
                duration = interval.inWholeMinutes, // Initial delay of interval in minutes
                timeUnit = TimeUnit.MINUTES
            ).build()

        workManager.enqueue(workRequest).await()
    }

    override suspend fun cancelAllSyncs() {
        // Implementation for cancelling all scheduled sync operations
        WorkManager.getInstance(context)
            .cancelAllWork()
            .await()
    }
}