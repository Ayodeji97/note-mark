@file:OptIn(ExperimentalTime::class)

package com.danzucker.notemark.note.data.note.datastore.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.danzucker.notemark.note.domain.note.datastore.settings.SettingsPreferences
import com.danzucker.notemark.note.domain.note.sync.SyncInterval
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


class DataStoreSettingsPreferences(
    private val dataStore: DataStore<Preferences>
) : SettingsPreferences {

    override suspend fun setSyncInterval(syncInterval: SyncInterval) {
        dataStore.edit { preferences ->
            preferences[SYNC_INTERVAL_KEY] = syncInterval.name
        }
    }

    override fun observeSyncInterval(): Flow<SyncInterval> {
        return dataStore.data
            .map { preferences ->
                preferences[SYNC_INTERVAL_KEY]?.let {
                    SyncInterval.valueOf(it)
                } ?: SyncInterval.MANUAL
            }
            .distinctUntilChanged()
    }

    override suspend fun setAutoSyncEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[AUTO_SYNC_ENABLED_KEY] = enabled
        }
    }

    override fun observeAutoSyncEnabled(): Flow<Boolean> {
        return dataStore.data
            .map { preferences ->
                preferences[AUTO_SYNC_ENABLED_KEY] ?: false
            }
            .distinctUntilChanged()
    }

    override suspend fun setLastSyncTimestamp(timestamp: Instant) {
        dataStore.edit { preferences ->
            preferences[LAST_SYNC_TIMESTAMP_KEY] = timestamp.toString()
        }
    }

    override fun observeLastSyncTimestamp(): Flow<Instant> {
        return dataStore.data
            .map { preferences ->
                preferences[LAST_SYNC_TIMESTAMP_KEY]?.let {
                    Instant.parse(it)
                } ?: Instant.DISTANT_PAST
            }
            .distinctUntilChanged()
    }

    override suspend fun clearSettings() {
        dataStore.edit { preferences ->
            preferences.remove(SYNC_INTERVAL_KEY)
            preferences.remove(AUTO_SYNC_ENABLED_KEY)
            preferences.remove(LAST_SYNC_TIMESTAMP_KEY)
        }
    }

    companion object {
        private val SYNC_INTERVAL_KEY = stringPreferencesKey("sync_interval")
        private val AUTO_SYNC_ENABLED_KEY = booleanPreferencesKey("auto_sync_enabled")
        private val LAST_SYNC_TIMESTAMP_KEY = stringPreferencesKey("last_sync_timestamp")
    }
}