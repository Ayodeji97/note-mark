package com.danzucker.notemark.auth.data

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val username: String
)
