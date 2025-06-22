package com.danzucker.notemark.auth.data

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)
