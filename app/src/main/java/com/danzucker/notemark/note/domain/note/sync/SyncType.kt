package com.danzucker.notemark.note.domain.note.sync

import com.danzucker.notemark.note.domain.note.local.NoteId
import com.danzucker.notemark.note.domain.note.model.Note
import kotlin.time.Duration

sealed class SyncType {
    data class FetchNotes(val interval: Duration) : SyncType()
    data class CreateNote(val note: Note) : SyncType()
    data class DeleteNote(val noteId: NoteId) : SyncType()
}