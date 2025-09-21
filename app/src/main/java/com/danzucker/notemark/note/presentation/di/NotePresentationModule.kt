package com.danzucker.notemark.note.presentation.di

import com.danzucker.notemark.note.presentation.notedetails.NoteDetailsViewModel
import com.danzucker.notemark.note.presentation.notelist.NoteViewModel
import com.danzucker.notemark.note.presentation.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val notePresentationModule = module {

    // ViewModels
    viewModelOf(::NoteViewModel)
    viewModelOf(::NoteDetailsViewModel)
    viewModelOf(::SettingsViewModel)
}