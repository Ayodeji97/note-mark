package com.danzucker.notemark.note.data.note.network.model

import kotlinx.serialization.Serializable

@Serializable
data class LogoutRequest(
    val refreshToken: String
)
