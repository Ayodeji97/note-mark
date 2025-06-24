package com.danzucker.notemark.auth.di

import com.danzucker.notemark.auth.data.NoteAuthRepository
import com.danzucker.notemark.auth.data.validator.EmailPatternValidator
import com.danzucker.notemark.auth.domain.AuthRepository
import com.danzucker.notemark.auth.domain.PatternValidator
import com.danzucker.notemark.auth.domain.UserDataValidator
import com.danzucker.notemark.auth.presentation.login.LoginViewModel
import com.danzucker.notemark.auth.presentation.register.RegisterViewModel
import com.danzucker.notemark.core.data.networking.HttpClientFactory
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authModule = module {

    singleOf(::EmailPatternValidator) bind PatternValidator::class
    singleOf(::UserDataValidator)

    singleOf(::NoteAuthRepository) bind AuthRepository::class

    // ViewModels
    viewModelOf(::RegisterViewModel)
    viewModelOf(::LoginViewModel)
}