@file:OptIn(ExperimentalTime::class)

package com.danzucker.notemark.note.domain.note.datastore.settings

import com.danzucker.notemark.note.domain.note.sync.SyncInterval
import kotlinx.coroutines.flow.Flow
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

interface SettingsPreferences {
    suspend fun setSyncInterval(syncInterval: SyncInterval)
    fun observeSyncInterval(): Flow<SyncInterval>
    suspend fun setAutoSyncEnabled(enabled: Boolean)
    fun observeAutoSyncEnabled(): Flow<Boolean>
    suspend fun setLastSyncTimestamp(timestamp: Instant)
    fun observeLastSyncTimestamp(): Flow<Instant>
    suspend fun clearSettings()
}