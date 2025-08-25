package com.danzucker.notemark.app

import android.app.Application
import com.danzucker.notemark.BuildConfig
import com.danzucker.notemark.app.di.appModule
import com.danzucker.notemark.auth.di.authModule
import com.danzucker.notemark.core.data.di.coreDataModule
import com.danzucker.notemark.core.database.di.databaseModule
import com.danzucker.notemark.note.data.note.di.noteDataModule
import com.danzucker.notemark.note.domain.di.noteDomainModule
import com.danzucker.notemark.note.presentation.di.notePresentationModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class NoteMarkApplication : Application() {

    val applicationScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        // Initialize any libraries or components here if needed
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@NoteMarkApplication)
            modules(
                appModule,
                authModule,
                notePresentationModule,
                noteDomainModule,
                noteDataModule,
                databaseModule,
                coreDataModule,
            )
        }
    }

}