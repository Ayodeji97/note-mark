@file:OptIn(ExperimentalTime::class)

package com.danzucker.notemark.note.presentation.notelist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danzucker.notemark.core.domain.sessionstorage.SessionStorage
import com.danzucker.notemark.core.domain.util.Result
import com.danzucker.notemark.core.presentation.util.UiText
import com.danzucker.notemark.note.domain.note.NoteRepository
import com.danzucker.notemark.note.domain.note.model.NoteSaveStatus
import com.danzucker.notemark.note.domain.note.util.generateUUID
import com.danzucker.notemark.note.models.NoteUi
import com.danzucker.notemark.note.presentation.notelist.mapper.toNote
import com.danzucker.notemark.note.presentation.notelist.mapper.toNoteUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class NoteViewModel(
    private val sessionStorage: SessionStorage,
    private val noteRepository: NoteRepository
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(NoteState())

    private val eventChannel = Channel<NoteEvent>()
    val events = eventChannel.receiveAsFlow()

    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                observeNotes()
                getProfileInitials()
                deleteDraftNotes()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = NoteState()
        )

    fun onAction(action: NoteAction) {
        when (action) {
            NoteAction.OnCreateNoteClick -> onCreateNoteClick()
            NoteAction.OnProfileClick -> Unit
            is NoteAction.OnDeleteNoteClick -> deleteNote(noteId = action.noteUiId)
            is NoteAction.OnNoteCardLongClick -> showConfirmationDialog(currentNoteId = action.noteUiId)
            NoteAction.OnCancelClick,
            NoteAction.OnDismissConfirmationDialog -> hideConfirmationDialog()
        }
    }

    private fun observeNotes() {
        noteRepository.getNotes()
            .onEach { notes ->
                val noteUiList = notes.map { it.toNoteUi() }
                _state.update {
                    it.copy(
                        notes = noteUiList,
                        isLoadingData = false,
                        hasNotes = noteUiList.isNotEmpty()
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun onCreateNoteClick() {
        viewModelScope.launch {
            val note = NoteUi(
                id = generateUUID(),
                title = "New Note",
                createdAt = Clock.System.now(),
                lastEditAt = Clock.System.now(),
                saveStatus = NoteSaveStatus.DRAFT
            )

            eventChannel.send(NoteEvent.OnCreateNoteClick(note.id))
            val result = noteRepository.createNote(note.toNote())

            if (result is Result.Error) {
                println("Error creating note...")
                // You might want to handle navigation back or show an error
            }
        }
    }

    private fun getProfileInitials() {
        viewModelScope.launch {
            val authInfo = withContext(Dispatchers.IO) {
                sessionStorage.get()
            }
            _state.update {
                it.copy(
                    userProfileInitials = authInfo?.username?.let { username ->
                        getUserInitials(username)
                    } ?: ""
                )
            }
        }
    }

    private fun deleteDraftNotes() {
        viewModelScope.launch {
            noteRepository.deleteDraftNotes()
        }
    }

    private fun deleteNote(noteId: String) {
        viewModelScope.launch {
            hideConfirmationDialog()
            noteRepository.deleteNote(noteId)
        }
    }

    private fun showConfirmationDialog(currentNoteId: String) {
        _state.update {
            it.copy(
                currentNoteId = currentNoteId,
                showConfirmationDialog = true
            )
        }
    }

    private fun hideConfirmationDialog() {
        _state.update {
            it.copy(
                currentNoteId = null,
                showConfirmationDialog = false
            )
        }
    }

    private fun getUserInitials(username: String): String {
        if (username.isBlank()) return ""

        val words = username.trim().split("\\s+".toRegex())

        return when {
            words.size == 1 -> words[0].take(2)
            else -> "${words.first().first()}${words.last().first()}"
        }.uppercase()
    }
}