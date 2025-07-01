package com.danzucker.notemark.note.presentation.preview

import com.danzucker.notemark.note.models.NoteUi

data object NotePreviewModel {
    val noteUi = NoteUi(
        id = "1",
        title = "Sample Note",
        content = "This is a sample note content for preview purposes. It can be longer to simulate real note content.",
        createdAt = "APR 3",
        lastEditAt = "APR 3"
    )
}