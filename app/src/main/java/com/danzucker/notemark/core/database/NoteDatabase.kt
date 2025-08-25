package com.danzucker.notemark.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.danzucker.notemark.core.database.dao.NoteDao
import com.danzucker.notemark.core.database.dao.NotePendingSyncDao
import com.danzucker.notemark.core.database.entity.DeletedNoteSyncEntity
import com.danzucker.notemark.core.database.entity.NoteEntity
import com.danzucker.notemark.core.database.entity.NotePendingSyncEntity

@Database(
    entities = [
        NoteEntity::class,
        NotePendingSyncEntity::class,
        DeletedNoteSyncEntity::class
    ],
    version = 1,
)
abstract class NoteDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao

    abstract val notePendingSyncDao: NotePendingSyncDao
}