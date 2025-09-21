@file:OptIn(ExperimentalTime::class)

package com.danzucker.notemark.note.data.note

import com.danzucker.notemark.core.data.networking.post
import com.danzucker.notemark.core.database.dao.NotePendingSyncDao
import com.danzucker.notemark.core.domain.sessionstorage.SessionStorage
import com.danzucker.notemark.core.domain.util.DataError
import com.danzucker.notemark.core.domain.util.EmptyResult
import com.danzucker.notemark.core.domain.util.Result
import com.danzucker.notemark.core.domain.util.asEmptyDataResult
import com.danzucker.notemark.note.data.note.mappers.toNote
import com.danzucker.notemark.note.data.note.network.model.LogoutRequest
import com.danzucker.notemark.note.domain.note.model.Note
import com.danzucker.notemark.note.domain.note.NoteRepository
import com.danzucker.notemark.note.domain.note.local.LocalNoteDataSource
import com.danzucker.notemark.note.domain.note.local.NoteId
import com.danzucker.notemark.note.domain.note.model.NoteSaveStatus
import com.danzucker.notemark.note.domain.note.model.Notes
import com.danzucker.notemark.note.domain.note.network.RemoteNoteDataSource
import com.danzucker.notemark.note.domain.note.sync.SyncNoteScheduler
import com.danzucker.notemark.note.domain.note.sync.SyncType
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.ExperimentalTime

private const val LOGOUT_ROUTE = "/api/auth/logout"

private const val TAG = "OfflineNoteRepository"

class OfflineFirstNoteRepository(
    private val localNoteDataSource: LocalNoteDataSource,
    private val remoteNoteDataSource: RemoteNoteDataSource,
    private val applicationScope: CoroutineScope,
    private val httpClient: HttpClient,
    private val sessionStorage: SessionStorage,
    private val notePendingSyncDao: NotePendingSyncDao,
    private val syncNoteScheduler: SyncNoteScheduler
) : NoteRepository {
    override fun getNotes(): Flow<List<Note>> {
        return localNoteDataSource.getNotes()
    }

    override suspend fun getNoteById(id: NoteId): Result<Note, DataError.Local> {
        return localNoteDataSource.getNoteById(id)
    }

    override suspend fun fetchNotes(): EmptyResult<DataError> {
        return when (val remoteNotesResult = remoteNoteDataSource.getNotes()) {
            is Result.Error -> remoteNotesResult.asEmptyDataResult()
            is Result.Success -> {
                /**
                 * Need to check if the there any stale between the local and remote data sources
                 * We can resolve this conflict by using the lastEditedAt field of the Note
                 */
                handleFetchedNotes(remoteNotes = remoteNotesResult.data)
                Result.Success(Unit)
            }
        }
    }

    private suspend fun handleFetchedNotes(
        remoteNotes: Notes
    ) {
        withContext(Dispatchers.IO) {
            try {
                val username = sessionStorage.get()?.username ?: return@withContext
                val syncNoteInformation = buildSyncNoteInformation(username, remoteNotes)

                val syncNoteActions = getSyncNoteActions(syncNoteInformation)
                applySyncNoteActions(syncNoteActions)
                executeRemoteUpdates(syncNoteActions.remoteUpdates)
            } catch (e: Exception) {
                if (e is CancellationException) {
                    throw e
                }
                Timber.tag(TAG).e("Error handling fetched notes: ${e.localizedMessage}")
            }
        }
    }

    private suspend fun buildSyncNoteInformation(
        userName: String,
        remoteNotes: Notes
    ): SyncNoteInformation {
        val localNotes = localNoteDataSource.getNotes().first()
        // Get pending sync states
        val pendingCreateNotes = notePendingSyncDao.getAllNotePendingSyncEntities(userName)
            .map { it.noteId }.toSet()
        val pendingDeleteNotes = notePendingSyncDao.getAllDeletedNoteSyncEntities(userName)
            .map { it.noteId }.toSet()

        return SyncNoteInformation(
            localNotes = localNotes,
            remoteNotes = remoteNotes.notes,
            pendingCreateNotes = pendingCreateNotes,
            pendingDeleteNotes = pendingDeleteNotes,
            localNotesMap = localNotes.associateBy { it.id },
            remoteNotesMap = remoteNotes.notes.associateBy { it.id }
        )
    }

    private fun getSyncNoteActions(syncNoteInformation: SyncNoteInformation): SyncNoteActions {
        val actions = SyncNoteActions()
        processLocalNotes(syncNoteInformation, actions)
        processRemoteNotes(syncNoteInformation, actions)
        return actions
    }

    private fun processLocalNotes(
        syncInfo: SyncNoteInformation,
        actions: SyncNoteActions
    ) {
        syncInfo.localNotes.forEach { localNote ->
            val remoteNote = syncInfo.remoteNotesMap[localNote.id]

            when {
                syncInfo.pendingDeleteNotes.contains(localNote.id) -> {
                    Timber.tag(TAG).d("Skipping local note pending deletion: ${localNote.id}")
                }

                remoteNote == null -> {
                    handleLocalNoteNotInRemote(localNote, syncInfo, actions)
                }

                else -> {
                    handleConflictBetweenLocalAndRemote(localNote, remoteNote, syncInfo, actions)
                }
            }
        }
    }

    private fun handleLocalNoteNotInRemote(
        localNote: Note,
        syncInfo: SyncNoteInformation,
        actions: SyncNoteActions
    ) {

        when {
            syncInfo.pendingCreateNotes.contains(localNote.id) -> {
                Timber.tag(TAG).d("Skipping local note pending creation: ${localNote.id}")
            }
            localNote.saveStatus == NoteSaveStatus.DRAFT -> {
                Timber.tag(TAG).d("Keeping draft note: ${localNote.id}")
            }
            else -> {
                actions.notesToDelete.add(localNote.id)
                Timber.tag(TAG)
                    .d("Marked for deletion local note not present remotely: ${localNote.id}")
            }
        }
    }

    private fun handleConflictBetweenLocalAndRemote(
        localNote: Note,
        remoteNote: Note,
        syncInfo: SyncNoteInformation,
        actions: SyncNoteActions
    ) {
        when {
            remoteNote.lastEditedAt > localNote.lastEditedAt -> {
                actions.notesToUpsert.add(remoteNote.copy(saveStatus = NoteSaveStatus.FINAL))
                Timber.tag(TAG)
                    .d("Marked for upsert local note from remote: ${localNote.id}")
            }

            localNote.lastEditedAt > remoteNote.lastEditedAt -> {
                if (!syncInfo.pendingCreateNotes.contains(localNote.id)) {
                    actions.remoteUpdates.add(localNote)
                    Timber.tag(TAG)
                        .d("Marked for remote update note from local: ${localNote.id}")
                }
            }

            else -> {
                handleEqualTimestamps(localNote, remoteNote, actions)
            }
        }

    }

    private fun handleEqualTimestamps(localNote: Note, remoteNote: Note, actions: SyncNoteActions) {
        if (localNote.saveStatus != NoteSaveStatus.FINAL) {
            actions.notesToUpsert.add(remoteNote.copy(saveStatus = NoteSaveStatus.FINAL))
            Timber.tag(TAG).d("Syncing save status for: ${localNote.id}")
        }
    }

    private fun processRemoteNotes(
        syncInfo: SyncNoteInformation,
        actions: SyncNoteActions
    ) {
        syncInfo.remoteNotes.forEach { remoteNote ->
            if (!syncInfo.localNotesMap.containsKey(remoteNote.id) &&
                !syncInfo.pendingDeleteNotes.contains(remoteNote.id)) {
                actions.notesToUpsert.add(remoteNote.copy(saveStatus = NoteSaveStatus.FINAL))
                Timber.tag(TAG).d("Adding new remote note: ${remoteNote.id}")
            }
        }
    }

    private suspend fun applySyncNoteActions(noteActions: SyncNoteActions) {
        if (noteActions.notesToUpsert.isNotEmpty()) {
            localNoteDataSource.upsertNotes(
                Notes(
                    notes = noteActions.notesToUpsert,
                    total = noteActions.notesToUpsert.size
                )
            )
            Timber.tag(TAG).d("Upserted ${noteActions.notesToUpsert.size} notes")
        }

        noteActions.notesToDelete.forEach { noteId ->
            localNoteDataSource.deleteNote(noteId)
        }
        if (noteActions.notesToDelete.isNotEmpty()) {
            Timber.tag(TAG).d("Deleted ${noteActions.notesToDelete.size} notes")
        }
    }

    private fun executeRemoteUpdates(remoteUpdates: List<Note>) {
        /**
         * Schedule remote updates for newer local notes
         */
        remoteUpdates.forEach { note ->
            applicationScope.launch {
                when (remoteNoteDataSource.postNote(note)) {
                    is Result.Success -> {
                        Timber.tag(TAG).d("Updated remote note: ${note.id}")
                        localNoteDataSource.upsertNote(note.copy(saveStatus = NoteSaveStatus.FINAL))
                    }
                    is Result.Error -> {
                        // Schedule sync if immediate update fails
                        syncNoteScheduler.scheduleSync(
                            type = SyncType.CreateNote(note = note)
                        )
                        Timber.tag(TAG).e("Failed to update remote, scheduled sync: ${note.id}")
                    }
                }
            }
        }
    }

    override suspend fun upsertNote(note: Note): EmptyResult<DataError> {
        val localResult = localNoteDataSource.upsertNote(note)

        if (localResult !is Result.Success) {
            return localResult.asEmptyDataResult()
        }

        val noteWithId = note.copy(id = localResult.data)
        val remoteResult = remoteNoteDataSource.postNote(noteWithId)

        return when (remoteResult) {
            is Result.Success -> {
//                applicationScope.async {
//                    localNoteDataSource.upsertNote(remoteResult.data.copy(
//                        saveStatus = NoteSaveStatus.FINAL
//                    )).asEmptyDataResult()
//                }.await()
                Result.Success(Unit)
            }

            is Result.Error -> {
                // If the remote operation fails, we schedule a sync operation
                applicationScope.launch {
                    syncNoteScheduler.scheduleSync(
                        type = SyncType.CreateNote(
                            note = noteWithId.copy(
                                saveStatus = NoteSaveStatus.PENDING
                            )
                        )
                    )
                }.join()
                // After scheduling the sync, we return success
                Result.Success(Unit)
            }
        }
    }

    override suspend fun syncPendingNotes() {
        withContext(Dispatchers.IO) {
            val username = sessionStorage.get()?.username ?: return@withContext

            val createdNotes = async {
                notePendingSyncDao.getAllNotePendingSyncEntities(username)
            }

            val deletedNotes = async {
                notePendingSyncDao.getAllDeletedNoteSyncEntities(username)
            }

            val createdNoteJobs = createdNotes
                .await()
                .map {
                    launch {
                        val note = it.note.toNote()
                        when (remoteNoteDataSource.postNote(note)) {
                            is Result.Success -> {
                                /**
                                 * If the note is successfully created on the remote server,
                                 * we can safely delete the pending sync entity
                                 */
                                applicationScope.launch {
                                    notePendingSyncDao.deleteNotePendingSyncEntity(it.noteId)
                                }
                                Timber.tag(TAG).d("Created note: ${it.noteId}")
                            }
                            is Result.Error -> {
                                Timber.tag(TAG)
                                    .e("Failed to create note remotely: ${it.noteId}")
                            }
                        }
                    }
                }

            val deletedNoteJobs = deletedNotes
                .await()
                .map {
                    launch {
                        val noteId = it.noteId
                        when (remoteNoteDataSource.deleteNote(noteId)) {
                            is Result.Success -> {
                                /**
                                 * If the note is successfully deleted on the remote server,
                                 * we can safely delete the pending sync entity
                                 */
                                applicationScope.launch {
                                    notePendingSyncDao.deleteDeletedNoteSyncEntity(noteId)
                                }
                                Timber.tag(TAG).d("Deleted note: $noteId")
                            }
                            is Result.Error -> {
                                Timber.tag(TAG)
                                    .e("Failed to delete note remotely: $noteId")
                            }
                        }
                    }
                }

            // Wait for all jobs to complete
            createdNoteJobs.joinAll()
            deletedNoteJobs.joinAll()
        }
    }

    override suspend fun deleteNote(id: NoteId) {
        localNoteDataSource.deleteNote(id)

        /**
         * There is an edge case where a note is created offline and
         * then deleted also in offline. In that case we don't need to sync anything
         */
        val isPendingSync = notePendingSyncDao.getNotePendingSyncEntity(id) != null
        if (isPendingSync) {
            notePendingSyncDao.deleteNotePendingSyncEntity(id)
            Timber.tag(TAG).d("Deleted pending sync for note: $id")
            return
        }

        val remoteResult = applicationScope.async {
            remoteNoteDataSource.deleteNote(id)
        }.await()

        if (remoteResult is Result.Error) {
            // Schedule a sync operation to delete the note
            applicationScope.launch {
                syncNoteScheduler.scheduleSync(
                    type = SyncType.DeleteNote(
                        noteId = id
                    )
                )
            }
        }
    }

    override suspend fun deleteDraftNotes() {
        localNoteDataSource.deleteDraftNotes()
    }

    override suspend fun deleteAllNotes() {
        localNoteDataSource.deleteAllNotes()
    }

    override suspend fun logout(): Result<Unit, DataError.Network> {
        val authInfo = sessionStorage.get()
        val result = httpClient.post<LogoutRequest, Unit>(
            route = LOGOUT_ROUTE,
            body = LogoutRequest(
                refreshToken = authInfo?.refreshToken.orEmpty()
            )
        )

        httpClient.plugin(Auth).providers.filterIsInstance<BearerAuthProvider>()
            .firstOrNull()
            ?.clearToken()

        return result.asEmptyDataResult()
    }
}

private data class SyncNoteInformation(
    val localNotes: List<Note>,
    val remoteNotes: List<Note>,
    val pendingCreateNotes: Set<String>,
    val pendingDeleteNotes: Set<String>,
    val localNotesMap: Map<String, Note>,
    val remoteNotesMap: Map<String, Note>
)

private data class SyncNoteActions(
    val notesToUpsert: MutableList<Note> = mutableListOf(),
    val notesToDelete: MutableList<NoteId> = mutableListOf(),
    val remoteUpdates: MutableList<Note> = mutableListOf()
)