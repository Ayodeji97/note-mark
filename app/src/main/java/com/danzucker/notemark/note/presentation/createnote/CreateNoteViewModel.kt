@file:OptIn(ExperimentalTime::class)

package com.danzucker.notemark.note.presentation.createnote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danzucker.notemark.note.domain.note.model.Note
import com.danzucker.notemark.note.domain.note.NoteRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class CreateNoteViewModel(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(CreateNoteState())

    private val eventChannel = Channel<CreateNoteEvent>()
    val events = eventChannel.receiveAsFlow()

    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = CreateNoteState()
        )

    fun onAction(action: CreateNoteAction) {
        when (action) {
            is CreateNoteAction.OnTitleTextChange -> onTitleTextChange(action.text)
            is CreateNoteAction.OnContentTextChange -> onContentTextChange(action.text)
            is CreateNoteAction.OnCloseClick -> {}
            is CreateNoteAction.OnSaveClick -> onSaveClick()

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
                id = UUID.randomUUID().toString(), // This should be passed from the navigation
                title = currentState.titleText,
                content = currentState.contentText,
                createdAt = Clock.System.now(), // Get this from the note creation logic on nvaigation to create note
                lastEditAt = Clock.System.now()
            )

            noteRepository.createNote(note = note)

            // navigate back
            eventChannel.send(CreateNoteEvent.NoteSuccessfullySaved)
        }
    }


}