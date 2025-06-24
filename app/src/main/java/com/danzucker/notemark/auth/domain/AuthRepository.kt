package com.danzucker.notemark.auth.domain

import com.danzucker.notemark.core.domain.util.DataError
import com.danzucker.notemark.core.domain.util.EmptyResult

interface AuthRepository {
    suspend fun login(email: String, password: String): EmptyResult<DataError.Network>
    suspend fun register(username: String, email: String, password: String): EmptyResult<DataError.Network>
}