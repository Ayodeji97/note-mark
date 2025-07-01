package com.danzucker.notemark.note.presentation.notelist

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.danzucker.notemark.R
import com.danzucker.notemark.core.presentation.designsystem.components.NoteMarkTopAppBar
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.core.presentation.util.ObserveAsEvents
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType
import com.danzucker.notemark.note.components.NoteList
import com.danzucker.notemark.note.components.NoteMarkGradientFloatingActionButton
import com.danzucker.notemark.note.components.ProfileInitials
import com.danzucker.notemark.note.presentation.preview.NotePreviewModel.noteUi
import org.koin.androidx.compose.koinViewModel


@Composable
fun NoteRoot(
    onNavigateToCreateNote: () -> Unit,
    viewModel: NoteViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            NoteEvent.OnCreateNoteClick -> onNavigateToCreateNote()
        }
    }

    NoteScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun NoteScreen(
    state: NoteState,
    onAction: (NoteAction) -> Unit,
) {

    val windowClass = currentWindowAdaptiveInfo().windowSizeClass

   Scaffold(
       containerColor = MaterialTheme.colorScheme.surface,
       topBar = {
           NoteMarkTopAppBar(
               title = stringResource(id = R.string.app_name),
               actionContent = {
                   ProfileInitials(
                       profileInitials = "DA", // replace later with state.profileInitials,
                       onProfileClick = {
                           onAction(NoteAction.OnProfileClick)
                       }
                   )
               }
           )
       },
       floatingActionButton = {
           NoteMarkGradientFloatingActionButton(
               onClick = {
                   onAction(NoteAction.OnCreateNoteClick)
               },
           )
       }
   ) { innerPadding ->
        NoteList(
            notes = state.notes,
            deviceScreenType = DeviceScreenType.fromWindowSizeClass(windowClass),
            modifier = Modifier
                .padding(innerPadding)
        )
   }
}

@Preview
@Composable
private fun NoteScreenPreview() {
    val noteUiPreview = remember {
        (1..15).map {
            noteUi.copy(
                id = it.toString()
            )
        }
    }
    NoteMarkTheme {
        NoteScreen(
            state = NoteState(
                notes = noteUiPreview
            ),
            onAction = {}
        )
    }
}