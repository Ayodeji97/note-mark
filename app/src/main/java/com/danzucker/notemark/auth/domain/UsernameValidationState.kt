package com.danzucker.notemark.auth.domain

data class UsernameValidationState(
    val hasLessThanThreeCharacters: Boolean = false,
    val hasMoreThanTwentyCharacters: Boolean = false,
    val hasValidCharacters: Boolean = false
)
