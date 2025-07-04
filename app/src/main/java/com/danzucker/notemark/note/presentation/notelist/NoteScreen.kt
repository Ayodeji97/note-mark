@file:OptIn(ExperimentalTime::class, ExperimentalTime::class, ExperimentalTime::class,
    ExperimentalTime::class
)

package com.danzucker.notemark.note.presentation.notelist
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.danzucker.notemark.R
import com.danzucker.notemark.core.presentation.designsystem.components.NoteMarkTopAppBar
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.core.presentation.util.ObserveAsEvents
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType
import com.danzucker.notemark.note.components.NoteList
import com.danzucker.notemark.note.components.NoteListAlertDialog
import com.danzucker.notemark.note.components.NoteMarkEmptyScreen
import com.danzucker.notemark.note.components.NoteMarkGradientFloatingActionButton
import com.danzucker.notemark.note.components.ProfileInitials
import com.danzucker.notemark.note.presentation.preview.NotePreviewModel.noteUi
import org.koin.androidx.compose.koinViewModel
import kotlin.time.ExperimentalTime


@Composable
fun NoteRoot(
    onNavigateToCreateNote: (String?) -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: NoteViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is NoteEvent.OnCreateNoteClick -> onNavigateToCreateNote(event.noteId)
        }
    }

    NoteScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is NoteAction.OnNoteCardClick -> onNavigateToCreateNote(action.noteUiId)
                is NoteAction.OnSettingsClick -> onNavigateToSettings()
                else -> Unit
            }
            viewModel.onAction(action)
        }
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
                   Row(
                       horizontalArrangement = Arrangement.spacedBy(11.dp)
                   ) {
                       IconButton(
                           onClick = {
                                 onAction(NoteAction.OnSettingsClick)
                           }
                       ) {
                           Icon(
                               imageVector = Icons.Outlined.Settings,
                               contentDescription = stringResource(R.string.settings),
                               tint = MaterialTheme.colorScheme.onSurface
                           )
                       }

                       ProfileInitials(
                           profileInitials = state.userProfileInitials,
                           onProfileClick = {
                               onAction(NoteAction.OnProfileClick)
                           }
                       )
                   }

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
       Column(
           modifier = Modifier
               .fillMaxSize()
               .padding(innerPadding)
       ) {
           when {
               state.isLoadingData -> {
                   CircularProgressIndicator(
                       modifier = Modifier
                           .fillMaxWidth()
                           .wrapContentSize(),
                       color = MaterialTheme.colorScheme.primary
                   )
               }
               !state.hasNotes -> {
                   NoteMarkEmptyScreen(
                       modifier = Modifier
                   )
               }
               else -> {
                   NoteList(
                       notes = state.notes,
                       deviceScreenType = DeviceScreenType.fromWindowSizeClass(windowClass),
                       onNoteClick = {
                           onAction(NoteAction.OnNoteCardClick(noteUiId = it))
                       },
                       onNoteLongClick = {
                           onAction(NoteAction.OnNoteCardLongClick(noteUiId = it))
                       }
                   )

               }
           }
       }

       if (state.showConfirmationDialog) {
           NoteListAlertDialog(
               title = stringResource(id = R.string.delete_note_confirmation_title),
               body = stringResource(id = R.string.delete_note_confirmation_body),
               confirmText = stringResource(R.string.delete),
               dismissText = stringResource(R.string.cancel),
               onDismissClick = {
                   onAction(NoteAction.OnDismissConfirmationDialog)
               },
               onConfirmClick = {
                   onAction(NoteAction.OnDeleteNoteClick(noteUiId = state.currentNoteId ?: ""))
               }
           )
       }
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