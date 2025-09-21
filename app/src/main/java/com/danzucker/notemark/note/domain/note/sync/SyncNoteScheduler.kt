package com.danzucker.notemark.note.domain.note.sync


interface SyncNoteScheduler {
    /**
     * Schedules a sync operation for notes.
     * @param type The type of sync operation to be scheduled.
     */
    suspend fun scheduleSync(type: SyncType)

    /**
     * Cancels all scheduled sync operations for notes.
     */
    suspend fun cancelAllSyncs()
}