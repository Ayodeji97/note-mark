package com.danzucker.notemark.app.di

import com.danzucker.notemark.app.MainViewModel
import com.danzucker.notemark.app.NoteMarkApplication
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<CoroutineScope> {
        (androidApplication() as NoteMarkApplication).applicationScope
    }

    viewModelOf(::MainViewModel)
}