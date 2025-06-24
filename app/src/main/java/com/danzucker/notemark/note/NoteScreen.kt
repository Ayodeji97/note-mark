package com.danzucker.notemark.note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme

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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Note Screen",
            style = MaterialTheme.typography.headlineLarge,
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