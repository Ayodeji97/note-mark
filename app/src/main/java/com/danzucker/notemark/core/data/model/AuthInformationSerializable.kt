package com.danzucker.notemark.core.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthInformationSerializable(
    val accessToken: String,
    val refreshToken: String,
    val username: String
)
