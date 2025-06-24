package com.danzucker.notemark.core.data.sessionstorage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import com.danzucker.notemark.core.data.model.AuthInformationSerializable
import com.danzucker.notemark.core.data.toAuthInformation
import com.danzucker.notemark.core.domain.model.AuthInformation
import com.danzucker.notemark.core.domain.sessionstorage.SessionStorage
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.danzucker.notemark.core.data.toAuthInformationSerializable


class DataStoreSessionStorage(
    private val dataStore: DataStore<Preferences>
) : SessionStorage  {

    override suspend fun get(): AuthInformation? {
        return try {
            val preferences = dataStore.data.first()
            preferences[KEY_AUTH_INFO]?.let { json ->
                Json.decodeFromString<AuthInformationSerializable>(json).toAuthInformation()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun set(info: AuthInformation?) {
        dataStore.edit { preferences ->
            if (info == null) {
                preferences.remove(KEY_AUTH_INFO)
            } else {
                val json = Json.encodeToString(info.toAuthInformationSerializable())
                preferences[KEY_AUTH_INFO] = json
            }
        }
    }

    companion object {
        private val KEY_AUTH_INFO = stringPreferencesKey("key_auth_info")
    }
}