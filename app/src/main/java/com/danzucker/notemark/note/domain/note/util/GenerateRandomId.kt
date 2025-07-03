package com.danzucker.notemark.note.domain.note.util

import java.util.UUID

fun generateUUID(): String {
    return UUID.randomUUID().toString()
}