package com.danzucker.notemark.core.domain.model

data class AuthInformation(
    val accessToken: String,
    val refreshToken: String,
    val username: String
)
