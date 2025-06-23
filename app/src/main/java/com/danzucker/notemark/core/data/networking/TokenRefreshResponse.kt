package com.danzucker.notemark.core.data.networking

import kotlinx.serialization.Serializable

@Serializable
data class TokenRefreshResponse(
    val accessToken: String,
    val refreshToken: String
)
