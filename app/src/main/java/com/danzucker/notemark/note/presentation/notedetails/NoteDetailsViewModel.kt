@file:OptIn(ExperimentalTime::class)

package com.danzucker.notemark.note.presentation.notedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danzucker.notemark.R
import com.danzucker.notemark.core.domain.util.Result
import com.danzucker.notemark.core.presentation.util.UiText
import com.danzucker.notemark.note.domain.note.model.Note
import com.danzucker.notemark.note.domain.note.NoteRepository
import com.danzucker.notemark.note.domain.note.model.NoteSaveStatus
import com.danzucker.notemark.note.presentation.notedetails.screenMode.ScreenMode
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class NoteDetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val noteRepository: NoteRepository
) : ViewModel() {

    private var hasLoadedInitialData = false
    private var autoHideJob: Job? = null

    private val noteId = savedStateHandle.get<String>("noteId")

    private val _state = MutableStateFlow(NoteDetailsState())

    private val eventChannel = Channel<NoteDetailsEvent>()
    val events = eventChannel.receiveAsFlow()

    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                observeNote()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = NoteDetailsState()
        )

    fun onAction(action: NoteDetailsAction) {
        when (action) {
            is NoteDetailsAction.OnTitleTextChange -> onTitleTextChange(action.text)
            is NoteDetailsAction.OnContentTextChange -> onContentTextChange(action.text)
            is NoteDetailsAction.OnSaveClick -> onSaveClick()
            is NoteDetailsAction.OnKeepEditingClick -> hideDiscardConfirmationDialog()
            is NoteDetailsAction.OnDiscardNoteDetailsClick -> onDiscardNoteClick()
            is NoteDetailsAction.OnCloseClick,
            is NoteDetailsAction.OnBacK -> onCloseClick()

            is NoteDetailsAction.OnEditModeClick -> switchToEditMode()
            is NoteDetailsAction.OnReaderModeClick -> if (state.value.isReaderMode) {
                switchToViewMode()
            } else {
                switchToReaderMode()
            }

            is NoteDetailsAction.OnViewModeClick -> switchToViewMode()
            is NoteDetailsAction.OnReaderScreenTap -> onReaderScreenTap()
            is NoteDetailsAction.OnReaderScrollStart -> onReaderScrollStart()
        }
    }


    private fun observeNote() {
        if (noteId == null) {
            // If no noteId is provided, we are creating a new note
            _state.update {
                it.copy(
                    originalText = "",
                    originalContext = "",
                )
            }
            return
        }

        viewModelScope.launch {
            when (val noteResult = noteRepository.getNoteById(noteId)) {
                is Result.Success -> {
                    val note = noteResult.data
                    _state.update {
                        it.copy(
                            id = note.id,
                            titleText = note.title,
                            contentText = note.content,
                            originalText = note.title,
                            originalContext = note.content,
                            createdAt = note.createdAt,
                            lastEditAt = note.lastEditedAt,
                            saveStatus = note.saveStatus,
                            screenMode = if (note.saveStatus == NoteSaveStatus.DRAFT) {
                                ScreenMode.Edit // Switch to Edit mode if it's a draft note
                            } else {
                                ScreenMode.View // Default to View mode when loading an existing note
                            }
                        )
                    }
                }

                is Result.Error -> {
                    _state.update {
                        it.copy(
                            errorText = UiText.StringResourceWithArgs(R.string.unable_to_retrieve_note)
                        )
                    }
                }
            }
        }
    }

    private fun switchToViewMode() {
        // If we are switching to view mode, we reset the reader mode orientation
        if (state.value.isReaderMode) {
            viewModelScope.launch {
                eventChannel.send(NoteDetailsEvent.ResetOrientation)
            }
        }

        _state.update {
            it.copy(
                screenMode = ScreenMode.View
            )
        }
    }

    private fun switchToReaderMode() {
        _state.update {
            it.copy(
                screenMode = ScreenMode.Reader,
                isReaderUiVisible = true
            )
        }

        startAutoHideReaderUiTimer()

        // When switching to reader mode, we request landscape orientation
        viewModelScope.launch {
            eventChannel.send(NoteDetailsEvent.RequestLandscapeOrientation)
        }
    }

    private fun switchToEditMode() {
        _state.update {
            it.copy(
                screenMode = ScreenMode.Edit
            )
        }
    }

    private fun onReaderScreenTap() {
        val currentState = state.value
        if (currentState.isReaderMode && currentState.isReaderUiVisible) {
            hideReaderUi()
        } else {
            showReaderUi()
            startAutoHideReaderUiTimer()
        }
    }

    private fun onReaderScrollStart() {
        val currentState = state.value
        // When scrolling starts, we hide the reader UI to avoid distractions
        if (currentState.isReaderMode && currentState.isReaderUiVisible) {
            hideReaderUi()
        }
    }

    private fun showReaderUi() {
        _state.update {
            it.copy(
                isReaderUiVisible = true
            )
        }
    }
    private fun startAutoHideReaderUiTimer() {
        autoHideJob?.cancel() // Cancel any existing job
        autoHideJob = viewModelScope.launch {
            delay(5000) // Auto-hide after 5 seconds
            hideReaderUi()
        }
    }
    private fun hideReaderUi() {
        autoHideJob?.cancel()
        _state.update {
            it.copy(
                isReaderUiVisible = false
            )
        }
    }

    private fun onCloseClick() {
        when (state.value.screenMode) {
            ScreenMode.Edit -> {
                if (hasNoteChanges()) {
                    showDiscardConfirmationDialog()
                } else {
                    handleEmptyNoteAndNavigateBack()
                }
            }

            ScreenMode.Reader, ScreenMode.View -> handleEmptyNoteAndNavigateBack()
        }
    }

    private fun handleEmptyNoteAndNavigateBack() {
        viewModelScope.launch {
            val currentState = state.value
            if (!hasEmptyNoteTitleAndContent() && currentState.isDraft) {
                // We don't want to save an empty note or a draft note, so we navigate back
                noteRepository.deleteNote(currentState.id)
            }
            if (currentState.isViewMode || currentState.isReaderMode) {
                // If we are in view or reader mode, we just navigate back
                eventChannel.send(NoteDetailsEvent.NavigateBack)
            } else {
                // If we are in edit mode, we switch to view mode
                switchToViewMode()
            }
            if (currentState.showDiscardConfirmationDialog) {
                hideDiscardConfirmationDialog()
            }
        }
    }

    private fun onDiscardNoteClick() {
        viewModelScope.launch {
            val currentState = state.value
            if (currentState.isDraft) {
                noteRepository.deleteNote(currentState.id)
            }
            switchToViewMode()
            restoreNoteDetails()
            if (currentState.showDiscardConfirmationDialog) {
                hideDiscardConfirmationDialog()
            }
        }
    }

    private fun restoreNoteDetails() {
        val currentState = state.value
        _state.update {
            it.copy(
                titleText = currentState.originalText,
                contentText = currentState.originalContext,
                showDiscardConfirmationDialog = false
            )
        }
    }

    private fun hasNoteChanges(): Boolean {
        val currentState = state.value
        return currentState.titleText != currentState.originalText ||
                currentState.contentText != currentState.originalContext
    }

    private fun hasEmptyNoteTitleAndContent(): Boolean {
        val currentState = state.value
        return currentState.titleText.isBlank() && currentState.contentText.isBlank()
    }

    private fun showDiscardConfirmationDialog() {
        _state.update {
            it.copy(
                showDiscardConfirmationDialog = true
            )
        }
    }

    private fun hideDiscardConfirmationDialog() {
        _state.update {
            it.copy(
                showDiscardConfirmationDialog = false
            )
        }
    }

    private fun onTitleTextChange(text: String) {
        _state.update {
            it.copy(
                titleText = text
            )
        }
    }

    private fun onContentTextChange(text: String) {
        _state.update {
            it.copy(
                contentText = text
            )
        }
    }

    private fun onSaveClick() {
        viewModelScope.launch {
            val currentState = state.value

            val note = Note(
                id = currentState.id, // We can get it from the state
                title = currentState.titleText,
                content = currentState.contentText,
                createdAt = if (currentState.id.isEmpty()) {
                    Clock.System.now() // If it's a new note, set createdAt to now
                } else {
                    currentState.createdAt // Otherwise, keep the original createdAt
                },
                lastEditedAt = Clock.System.now()
            )

            when (noteRepository.upsertNote(note = note)) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            id = note.id, // Update the state with the new note ID
                            originalText = note.title,
                            originalContext = note.content,
                            createdAt = note.createdAt,
                            lastEditAt = note.lastEditedAt,
                            saveStatus = note.saveStatus
                        )
                    }
                    switchToViewMode()
                }

                is Result.Error -> {
                    _state.update {
                        it.copy(
                            errorText = UiText.StringResourceWithArgs(R.string.unable_to_save_note)
                        )
                    }
                    // Optionally, you can handle navigation back or show an error
                    eventChannel.send(NoteDetailsEvent.FailedToSaveNoteDetails)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        autoHideJob?.cancel()
    }
}