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
import kotlinx.coroutines.channels.Channel
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
            is NoteDetailsAction.OnBacK-> onCloseClick()
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
                            saveStatus = note.saveStatus,
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


    private fun onCloseClick() {
        if (hasNoteChanges()) {
            showDiscardConfirmationDialog()
        } else {
            handleEmptyNoteAndNavigateBack()
        }
    }

    private fun handleEmptyNoteAndNavigateBack() {
        viewModelScope.launch {
            if (!hasEmptyNoteTitleAndContent() && isDraftNote()) {
                // We don't want to save an empty note or a draft note, so we navigate back
                noteRepository.deleteNote(state.value.id)
            }
            eventChannel.send(NoteDetailsEvent.NavigateBack)
        }
    }

    private fun onDiscardNoteClick() {
        viewModelScope.launch {
            if (isDraftNote()) {
                noteRepository.deleteNote(state.value.id)
            }
            eventChannel.send(NoteDetailsEvent.NavigateBack)
        }
    }

    private fun isDraftNote(): Boolean {
        return state.value.saveStatus == NoteSaveStatus.DRAFT
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
                createdAt = Clock.System.now(),
                lastEditAt = Clock.System.now(),
                saveStatus = NoteSaveStatus.FINAL
            )



            when (noteRepository.createNote(note = note)) {
                is Result.Success -> {
                    eventChannel.send(NoteDetailsEvent.NoteDetailsSuccessfullySaved)
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
}