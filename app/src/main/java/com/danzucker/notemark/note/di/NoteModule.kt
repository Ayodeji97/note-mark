package com.danzucker.notemark.note.di

import com.danzucker.notemark.note.data.note.OfflineFirstNoteRepository
import com.danzucker.notemark.note.data.note.local.RoomLocalNoteDataSource
import com.danzucker.notemark.note.data.note.network.KtorRemoteNoteDataSource
import com.danzucker.notemark.note.domain.note.NoteRepository
import com.danzucker.notemark.note.domain.note.local.LocalNoteDataSource
import com.danzucker.notemark.note.domain.note.network.RemoteNoteDataSource
import com.danzucker.notemark.note.presentation.notedetails.NoteDetailsViewModel
import com.danzucker.notemark.note.presentation.notelist.NoteViewModel
import com.danzucker.notemark.note.presentation.settings.SettingsViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val noteModule = module {
    singleOf(::RoomLocalNoteDataSource).bind<LocalNoteDataSource>()
    singleOf(::KtorRemoteNoteDataSource).bind<RemoteNoteDataSource>()
    singleOf(::OfflineFirstNoteRepository).bind<NoteRepository>()

    // ViewModels
    viewModelOf(::NoteViewModel)
    viewModelOf(::NoteDetailsViewModel)
    viewModelOf(::SettingsViewModel)
}