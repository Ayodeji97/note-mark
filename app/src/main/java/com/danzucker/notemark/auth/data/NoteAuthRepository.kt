package com.danzucker.notemark.auth.data

import com.danzucker.notemark.auth.domain.AuthRepository
import com.danzucker.notemark.core.data.networking.post
import com.danzucker.notemark.core.domain.model.AuthInformation
import com.danzucker.notemark.core.domain.sessionstorage.SessionStorage
import com.danzucker.notemark.core.domain.util.DataError
import com.danzucker.notemark.core.domain.util.EmptyResult
import com.danzucker.notemark.core.domain.util.Result
import com.danzucker.notemark.core.domain.util.asEmptyDataResult
import io.ktor.client.HttpClient

private const val LOGIN_ROUTE = "/api/auth/login"
private const val REGISTER_ROUTE = "/api/auth/register"

class NoteAuthRepository(
    private val httpClient: HttpClient,
    private val sessionStorage: SessionStorage
) : AuthRepository {
    override suspend fun login(email: String, password: String): EmptyResult<DataError.Network> {
        val result = httpClient.post<LoginRequest, LoginResponse>(
            route = LOGIN_ROUTE,
            body = LoginRequest(
                email = email,
                password = password
            )
        )

        if (result is Result.Success) {
            sessionStorage.set(
                AuthInformation(
                    accessToken = result.data.accessToken,
                    refreshToken = result.data.refreshToken,
                    username = result.data.username
                )
            )
        }
        return result.asEmptyDataResult()
    }

    override suspend fun register(username: String, email: String, password: String): EmptyResult<DataError.Network> {
       return httpClient.post<RegisterRequest, Unit>(
            route = REGISTER_ROUTE,
            body = RegisterRequest(
                username = username,
                email = email,
                password = password
            )
       )
    }
}