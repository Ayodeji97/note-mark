@file:OptIn(ExperimentalTime::class)

package com.danzucker.notemark.note.domain.note.model

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class Note(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: Instant,
    val lastEditedAt: Instant,
    val saveStatus: NoteSaveStatus = NoteSaveStatus.DRAFT
)

enum class NoteSaveStatus {
    DRAFT, // Not yet saved by user
    PENDING, // Saved but not yet synced with server
    FINAL // Synced with server
}