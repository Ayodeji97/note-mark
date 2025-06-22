package com.danzucker.notemark.core.domain.sessionstorage

import com.danzucker.notemark.core.domain.model.AuthInformation

interface SessionStorage {
    suspend fun get(): AuthInformation?
    suspend fun set(info: AuthInformation?)
}