package com.danzucker.notemark.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deleted_note_sync")
data class DeletedNoteSyncEntity(
    @PrimaryKey(autoGenerate = false)
    val noteId: String,
    val username: String,
)
