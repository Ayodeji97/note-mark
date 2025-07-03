@file:OptIn(ExperimentalTime::class)

package com.danzucker.notemark.note.presentation.preview

import com.danzucker.notemark.note.domain.note.model.NoteSaveStatus
import com.danzucker.notemark.note.models.NoteUi
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data object NotePreviewModel {
    val noteUi = NoteUi(
        id = "1",
        title = "Sample Note",
        content = "This is a sample note content for preview purposes. It can be longer to simulate real note content.",
        createdAt = Instant.parse("2023-04-01T10:15:30Z"),
        lastEditAt = Instant.parse("2023-04-01T10:15:30Z"),
        saveStatus = NoteSaveStatus.DRAFT
    )
}