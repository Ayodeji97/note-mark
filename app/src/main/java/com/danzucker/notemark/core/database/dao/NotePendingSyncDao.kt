package com.danzucker.notemark.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.danzucker.notemark.core.database.entity.DeletedNoteSyncEntity
import com.danzucker.notemark.core.database.entity.NotePendingSyncEntity

@Dao
interface NotePendingSyncDao {

    // CREATED NOTES
    @Query("SELECT * FROM note_pending_sync WHERE userName=:username")
    suspend fun getAllNotePendingSyncEntities(username: String): List<NotePendingSyncEntity>

    @Query("SELECT * FROM note_pending_sync WHERE noteId=:noteId")
    suspend fun getNotePendingSyncEntity(noteId: String): NotePendingSyncEntity?

    @Upsert
    suspend fun upsertNotePendingSyncEntity(entity: NotePendingSyncEntity)

    @Query("DELETE FROM note_pending_sync WHERE noteId=:noteId")
    suspend fun deleteNotePendingSyncEntity(noteId: String)


    // DELETED NOTES
    @Query("SELECT * FROM deleted_note_sync WHERE username=:username")
    suspend fun getAllDeletedNoteSyncEntities(username: String): List<DeletedNoteSyncEntity>

    @Upsert
    suspend fun upsertDeletedNoteSyncEntity(entity: DeletedNoteSyncEntity)

    @Query("DELETE FROM deleted_note_sync WHERE noteId=:noteId")
    suspend fun deleteDeletedNoteSyncEntity(noteId: String)
}