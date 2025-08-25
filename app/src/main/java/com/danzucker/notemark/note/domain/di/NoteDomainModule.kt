package com.danzucker.notemark.note.domain.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.danzucker.notemark.core.data.sessionstorage.settingsDataStore
import com.danzucker.notemark.note.data.note.datastore.settings.DataStoreSettingsPreferences
import com.danzucker.notemark.note.domain.note.datastore.settings.SettingsPreferences
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val noteDomainModule = module {
    single<DataStore<Preferences>> {
        get<Context>().settingsDataStore
    }

    singleOf(::DataStoreSettingsPreferences) bind SettingsPreferences::class
}