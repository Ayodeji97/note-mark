@file:OptIn(ExperimentalTime::class)

package com.danzucker.notemark.note.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danzucker.notemark.R
import com.danzucker.notemark.core.data.networkchecker.DeviceNetworkChecker
import com.danzucker.notemark.core.database.dao.NotePendingSyncDao
import com.danzucker.notemark.core.domain.sessionstorage.SessionStorage
import com.danzucker.notemark.core.domain.util.Result
import com.danzucker.notemark.core.presentation.util.UiText
import com.danzucker.notemark.note.domain.note.NoteRepository
import com.danzucker.notemark.note.domain.note.datastore.settings.SettingsPreferences
import com.danzucker.notemark.note.domain.note.mappers.toSyncInterval
import com.danzucker.notemark.note.domain.note.mappers.toSyncIntervalUi
import com.danzucker.notemark.note.domain.note.sync.SyncNoteScheduler
import com.danzucker.notemark.note.domain.note.sync.SyncType
import com.danzucker.notemark.note.presentation.settings.util.SyncIntervalUi
import com.danzucker.notemark.note.presentation.util.toFormattedLastSyncTimeStamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class SettingsViewModel(
    private val noteRepository: NoteRepository,
    private val syncNoteScheduler: SyncNoteScheduler,
    private val applicationScope: CoroutineScope,
    private val sessionStorage: SessionStorage,
    private val settingsPreferences: SettingsPreferences,
    private val deviceNetworkChecker: DeviceNetworkChecker,
    private val notePendingSyncDao: NotePendingSyncDao
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(SettingsState())

    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                observeSettings()
                observeNetworkChanges()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = SettingsState()
        )

    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.OnSyncIntervalClick -> toggleSyncIntervalDropdown()
            is SettingsAction.OnBackClick -> Unit // Handle back click in root composable (SettingsRoot)
            is SettingsAction.OnLogoutClick -> checkForUnsyncedChangesAndLogout()
            is SettingsAction.OnSyncIntervalItemSelected -> onSyncIntervalSelected(action.syncInterval)
            is SettingsAction.OnDismissSyncIntervalDropdown -> dismissSyncIntervalDropdown()
            SettingsAction.OnSyncDataClick -> onSyncDataClick()
            SettingsAction.OnConfirmLogout -> proceedWithLogout()
            SettingsAction.OnCancelLogout -> dismissLogoutDialog()
            SettingsAction.OnSyncAndLogout -> syncAndLogout()
        }
    }

    private fun observeSettings() {
        combine(
            settingsPreferences.observeSyncInterval(),
            settingsPreferences.observeLastSyncTimestamp()
        ) { savedSyncInterval, lastSyncTimestamp ->
            val syncIntervalUi = savedSyncInterval.toSyncIntervalUi()
            _state.update {
                it.copy(
                    syncIntervalText = syncIntervalUi.title,
                    selectedSyncInterval = syncIntervalUi,
                    lastSyncTimestamp = lastSyncTimestamp.toFormattedLastSyncTimeStamp()
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun observeNetworkChanges() {
        deviceNetworkChecker.isDeviceConnected()
            .onEach { isConnected ->
                _state.update {
                    it.copy(
                        isDeviceConnected = isConnected
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun onSyncIntervalSelected(syncInterval: SyncIntervalUi) {
        viewModelScope.launch {
            settingsPreferences.setSyncInterval(syncInterval.toSyncInterval())
            updateBackgroundSyncInterval(syncInterval = syncInterval)
            _state.update {
                it.copy(
                    syncIntervalText = syncInterval.title,
                    selectedSyncInterval = syncInterval,
                    showSyncIntervalDropdown = false
                )
            }
        }
    }

    private fun updateBackgroundSyncInterval(syncInterval: SyncIntervalUi) {
        viewModelScope.launch {
            when (syncInterval) {
                SyncIntervalUi.MANUAL -> syncNoteScheduler.cancelAllSyncs()
                else -> syncNoteScheduler.scheduleSync(
                    type = SyncType.FetchNotes(syncInterval.timeInterval)
                )
            }
        }
    }

    private fun onSyncDataClick() {
        val isConnected = _state.value.isDeviceConnected
        if (!isConnected) {
           _state.update {
               it.copy(
                   isSyncingData = false,
                   showError = true,
                   errorMessage = UiText.StringResourceWithArgs(R.string.network_error_message)
               )
           }
            return
        }

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isSyncingData = true,
                    showError = false
                )
            }

            noteRepository.syncPendingNotes()
            when (noteRepository.fetchNotes()) {
                is Result.Success -> {
                    val currentTime = Clock.System.now()
                    settingsPreferences.setLastSyncTimestamp(currentTime)

                    _state.update {
                        it.copy(
                            isSyncingData = false,
                            lastSyncTimestamp = currentTime.toFormattedLastSyncTimeStamp(),
                            showError = false
                        )
                    }
                }
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isSyncingData = false,
                            lastSyncTimestamp = UiText.StringResourceWithArgs(R.string.never_synced),
                            showError = true,
                            errorMessage = UiText.StringResourceWithArgs(R.string.failed_to_sync_data)
                        )
                    }
                }
            }
        }
    }


    private fun checkForUnsyncedChangesAndLogout() {
        val isConnected = _state.value.isDeviceConnected
        if (!isConnected) {
            _state.update {
                it.copy(
                    showError = true,
                    errorMessage = UiText.StringResourceWithArgs(R.string.offline_logout_message)
                )
            }
            return
        }

        viewModelScope.launch {
            // Check for unsynced changes
            try {
                val username = sessionStorage.get()?.username ?: return@launch
                val pendingNotes = notePendingSyncDao.getAllNotePendingSyncEntities(username)
                val deletedNotes = notePendingSyncDao.getAllDeletedNoteSyncEntities(username)

                if (pendingNotes.isNotEmpty() || deletedNotes.isNotEmpty()) {
                    // Show logout confirmation dialog
                    _state.update {
                        it.copy(
                            showLogoutConfirmationDialog = true,
                            showError = false
                        )
                    }
                } else {
                    // No unsynced changes, proceed with logout
                    proceedWithLogout()
                }
            } catch (e: Exception) {
                println("Error checking for unsynced changes: ${e.message}")
               proceedWithLogout()
            }

        }
    }

    private fun syncAndLogout() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isSyncingData = true,
                    showLogoutConfirmationDialog = false
                )
            }

            // Perform sync
            noteRepository.syncPendingNotes()
            noteRepository.fetchNotes()

            // Proceed with logout
            proceedWithLogout()
        }
    }

    private fun proceedWithLogout() {
        applicationScope.launch {
            try {
                noteRepository.deleteAllNotes()
                noteRepository.logout()
                sessionStorage.set(null)
                settingsPreferences.clearSettings()
                syncNoteScheduler.cancelAllSyncs()

                _state.update {
                    it.copy(
                        isSyncingData = false,
                        showError = false,
                        showLogoutConfirmationDialog = false
                    )
                }
            } catch (e: Exception) {
                println("Error during logout: ${e.message}")
                _state.update {
                    it.copy(
                        isSyncingData = false,
                        showError = true,
                        errorMessage = UiText.StringResourceWithArgs(R.string.error_message),
                        showLogoutConfirmationDialog = false
                    )
                }
            }

        }
    }

    private fun dismissLogoutDialog() {
        _state.update {
            it.copy(showLogoutConfirmationDialog = false)
        }
    }

    private fun toggleSyncIntervalDropdown() {
        _state.update {
            it.copy(showSyncIntervalDropdown = !it.showSyncIntervalDropdown)
        }
    }

    private fun dismissSyncIntervalDropdown() {
        _state.update {
            it.copy(showSyncIntervalDropdown = false)
        }
    }

    private fun onLogout() {
        val hasInternetConnection = _state.value.isDeviceConnected
        if (!hasInternetConnection) {
            _state.update {
                it.copy(
                    showError = true,
                    errorMessage = UiText.StringResourceWithArgs(R.string.offline_logout_message)
                )
            }
            return
        }

        applicationScope.launch {
            // Sync any pending notes before logging out
            _state.update {
                it.copy(
                    isSyncingData = true,
                    showError = false
                )
            }
            noteRepository.syncPendingNotes()
            noteRepository.fetchNotes()

            noteRepository.deleteAllNotes()
            noteRepository.logout()
            sessionStorage.set(null)
            settingsPreferences.clearSettings()
            syncNoteScheduler.cancelAllSyncs()
            _state.update {
                it.copy(
                    isSyncingData = false,
                    showError = false
                )
            }
        }
    }

}