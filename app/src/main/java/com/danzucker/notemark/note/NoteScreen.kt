package com.danzucker.notemark.note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.danzucker.notemark.R
import com.danzucker.notemark.core.presentation.designsystem.components.NoteTopAppBar
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.note.components.NoteMarkGradientFloatingActionButton
import com.danzucker.notemark.note.components.ProfileInitials

@Composable
fun NoteRoot(
    viewModel: NoteViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

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
   Scaffold(
       containerColor = MaterialTheme.colorScheme.surface,
       topBar = {
           NoteTopAppBar(
               title = stringResource(id = R.string.app_name),
               actionContent = {
                   ProfileInitials(
                       profileInitials = "DA", // replace later with state.profileInitials,
                       onProfileClick = {
                           onAction( NoteAction.OnProfileClick)
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
        Box(
            modifier = Modifier.padding(innerPadding)
        )
   }
}

@Preview
@Composable
private fun Preview() {
    NoteMarkTheme {
        NoteScreen(
            state = NoteState(),
            onAction = {}
        )
    }
}