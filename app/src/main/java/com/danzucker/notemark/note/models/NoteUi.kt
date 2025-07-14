@file:OptIn(ExperimentalTime::class)

package com.danzucker.notemark.note.models

import com.danzucker.notemark.note.domain.note.model.NoteSaveStatus
import com.danzucker.notemark.note.presentation.util.toReadableDate
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class NoteUi(
    val id: String,
    val title: String,
    val content: String = "",
    val createdAt: Instant,
    val lastEditAt: Instant,
    val saveStatus: NoteSaveStatus
) {
    val formattedCreatedAt: String
        get() = createdAt.toReadableDate()

    val formattedLastEditAt: String
        get() = lastEditAt.toReadableDate()
}
