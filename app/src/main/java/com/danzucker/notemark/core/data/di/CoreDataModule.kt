package com.danzucker.notemark.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.danzucker.notemark.core.data.networking.HttpClientFactory
import com.danzucker.notemark.core.data.sessionstorage.DataStoreSessionStorage
import com.danzucker.notemark.core.data.sessionstorage.authDataStore
import com.danzucker.notemark.core.domain.sessionstorage.SessionStorage
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single<DataStore<Preferences>> {
        get<Context>().authDataStore
    }

    single {
        HttpClientFactory(get()).build()
    }

    singleOf(::DataStoreSessionStorage) bind SessionStorage::class
}