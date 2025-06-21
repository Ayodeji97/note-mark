package com.danzucker.notemark.auth.domain

interface PatternValidator {
    fun matches(value: String): Boolean
}