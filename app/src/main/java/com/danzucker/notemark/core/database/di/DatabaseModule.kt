package com.danzucker.notemark.core.database.di

import androidx.room.Room
import com.danzucker.notemark.core.database.NoteDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            NoteDatabase::class.java,
            "note.db"
        ).build()
    }

    single { get<NoteDatabase>().noteDao }
    // NotePendingSyncDao is used to manage notes that are pending synchronization
    // with the server, such as newly created notes or updates that haven't been synced yet.
    single { get<NoteDatabase>().notePendingSyncDao }
}