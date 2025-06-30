package com.danzucker.notemark.note.presentation.notelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danzucker.notemark.note.domain.note.NoteRepository
import com.danzucker.notemark.note.presentation.notelist.mapper.toNoteUi
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

class NoteViewModel(
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
        }
    }

    private fun observeNotes() {
       noteRepository.getNotes()
           .onEach { notes ->
               val noteUiList = notes.map { it.toNoteUi() }
               _state.update {
                   it.copy(
                       notes = noteUiList
                   )
               }
           }
           .launchIn(viewModelScope)
    }

    private fun onCreateNoteClick() {
        viewModelScope.launch {
            eventChannel.send(NoteEvent.OnCreateNoteClick)
        }
    }
}