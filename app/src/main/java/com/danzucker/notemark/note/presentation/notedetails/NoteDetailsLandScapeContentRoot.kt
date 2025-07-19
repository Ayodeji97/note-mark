@file:OptIn(ExperimentalTime::class)

package com.danzucker.notemark.note.presentation.notedetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.notemark.R
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.note.components.SaveNoteButton
import com.danzucker.notemark.note.presentation.notedetails.components.NoteMarkDetailsBackNavigation
import kotlin.time.ExperimentalTime

@Composable
fun NoteDetailsLandScapeContentRoot(
    state: NoteDetailsState,
    onAction: (NoteDetailsAction) -> Unit,
    descriptionFocusRequester: FocusRequester,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize(),
    ) {

        LeftNavigationSection(
            state = state,
            onAction = onAction,
            modifier = Modifier
                .weight(1.2f)
        )

        NoteDetailsLandScapeContent(
            state = state,
            onAction = onAction,
            descriptionFocusRequester = descriptionFocusRequester,
            focusManager = focusManager,
            modifier = Modifier
                .weight(4f)
                .padding(
                    start = 16.dp,
                    top = 10.dp
                )
        )

        RightNavigationSection(
            state = state,
            onAction = onAction,
            modifier = Modifier
                .weight(1.2f)
        )
    }
}


@Composable
private fun LeftNavigationSection(
    state: NoteDetailsState,
    onAction: (NoteDetailsAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = when {
            state.isEditMode -> Alignment.CenterHorizontally
            else -> Alignment.Start
        }
    ) {
        if (state.isEditMode) {
            EditModeCloseButton(onAction)
        } else {
            BackNavigationIfVisible(state, onAction)
        }
    }
}

@Composable
private fun RightNavigationSection(
    state: NoteDetailsState,
    onAction: (NoteDetailsAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {

        if (state.isEditMode) {
            SaveNoteButton(
                text = stringResource(R.string.save_note).uppercase(),
                onClick = { onAction(NoteDetailsAction.OnSaveClick) }
            )
        } else {
            Spacer(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun EditModeCloseButton(onAction: (NoteDetailsAction) -> Unit) {
    IconButton(onClick = { onAction(NoteDetailsAction.OnCloseClick) }) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = stringResource(R.string.close_note),
        )
    }
}


@Composable
private fun BackNavigationIfVisible(
    state: NoteDetailsState,
    onAction: (NoteDetailsAction) -> Unit
) {
    val isVisible = when {
        state.isReaderMode -> state.isReaderUiVisible
        else -> true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(delayMillis = 300)),
        exit = fadeOut(animationSpec = tween(delayMillis = 300))
    ) {
        NoteMarkDetailsBackNavigation(
            onBackClick = { onAction(NoteDetailsAction.OnBacK) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun NoteDetailsLandScapeContentRootPreview() {
    NoteMarkTheme {
        val descriptionFocusRequester = remember {
            FocusRequester()
        }
        val focusManager = LocalFocusManager.current
        NoteDetailsLandScapeContentRoot(
            state = NoteDetailsState(
                titleText = "Sample Note Title",
                contentText = "This is a sample note content for landscape mode."
            ),
            onAction = {},
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White),
            descriptionFocusRequester = descriptionFocusRequester,
            focusManager = focusManager,
        )
    }
}