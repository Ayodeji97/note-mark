package com.danzucker.notemark.core.data.model

data class AuthInformationSerializable(
    val accessToken: String,
    val refreshToken: String,
    val username: String
)
