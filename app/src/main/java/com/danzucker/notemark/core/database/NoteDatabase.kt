package com.danzucker.notemark.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.danzucker.notemark.core.database.dao.NoteDao
import com.danzucker.notemark.core.database.entity.NoteEntity

@Database(
    entities = [NoteEntity::class],
    version = 1,
)
abstract class NoteDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao
}