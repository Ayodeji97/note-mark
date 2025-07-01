package com.danzucker.notemark.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.danzucker.notemark.core.database.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM note_db ORDER BY lastEditAt DESC")
    fun getNotes(): Flow<List<NoteEntity>>

    @Upsert
    suspend fun upsertNote(note: NoteEntity): Long

    @Upsert
    suspend fun upsertNotes(note: List<NoteEntity>)

    @Query("DELETE FROM note_db WHERE id = :id")
    suspend fun deleteNoteById(id: String)

    @Query("DELETE FROM note_db")
    suspend fun deleteAllNotes()
}