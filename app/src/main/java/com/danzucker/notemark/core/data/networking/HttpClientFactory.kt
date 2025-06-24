package com.danzucker.notemark.core.data.networking

import com.danzucker.notemark.BuildConfig
import com.danzucker.notemark.core.domain.model.AuthInformation
import com.danzucker.notemark.core.domain.sessionstorage.SessionStorage
import com.danzucker.notemark.core.domain.util.Result
import com.danzucker.notemark.core.domain.util.asEmptyDataResult
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber


private const val REFRESH_TOKEN_ENDPOINT = "/api/auth/refresh"

class HttpClientFactory(
    private val sessionStorage: SessionStorage
) {

    fun build(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Timber.d(message)
                    }
                }
                level = LogLevel.ALL
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
                header("X-User-Email", BuildConfig.X_User_Email)
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        val authInfo = sessionStorage.get()
                        BearerTokens(
                            accessToken = authInfo?.accessToken ?: "",
                            refreshToken = authInfo?.refreshToken ?: ""
                        )
                    }
                    refreshTokens {
                        val authInfo = sessionStorage.get()
                        val response = client.post<TokenRefreshRequest, TokenRefreshResponse>(
                            route = REFRESH_TOKEN_ENDPOINT,
                            body = TokenRefreshRequest(
                                refreshToken = authInfo?.refreshToken ?: ""
                            )
                        )

                        Timber.d("New access token: ${response.asEmptyDataResult()}")
                        if (response is Result.Success) {
                            val newAuthInformation = AuthInformation(
                                accessToken = response.data.accessToken,
                                refreshToken = response.data.refreshToken,
                                username = authInfo?.username ?: "",
                            )
                            sessionStorage.set(newAuthInformation)

                            BearerTokens(
                                accessToken = newAuthInformation.accessToken,
                                refreshToken = newAuthInformation.refreshToken
                            )
                        } else {
                            BearerTokens(
                                accessToken = "",
                                refreshToken = ""
                            )
                        }
                    }
                }
            }
        }
    }
}