package com.danzucker.notemark.note.presentation.notedetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import com.danzucker.notemark.R
import com.danzucker.notemark.core.presentation.designsystem.components.NoteMarkTopAppBar
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.fontSizeMedium16
import com.danzucker.notemark.note.components.SaveNoteButton


@Composable
fun NoteDetailsPortraitContentRoot(
    state: NoteDetailsState,
    onAction: (NoteDetailsAction) -> Unit,
    descriptionFocusRequester: FocusRequester,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
) {

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        topBar = {
            AnimatedVisibility(
                visible = when {
                    state.isReaderMode -> state.isReaderUiVisible
                    else -> true
                },
                enter = fadeIn(animationSpec = tween(delayMillis = 300)),
                exit = fadeOut(animationSpec = tween(delayMillis = 300))
            ) {
                when {
                    state.isReaderMode || state.isViewMode -> {
                        NoteMarkTopAppBar(
                            modifier = Modifier.offset { IntOffset(x = (-16), y = 0) },
                            title = stringResource(R.string.all_notes).uppercase(),
                            onTitleClick = { onAction(NoteDetailsAction.OnBacK) },
                            titleTextSize = fontSizeMedium16,
                            titleColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            titleTextOffset = IntOffset(x = (-16), y = 0),
                            navigationIcon = {
                                IconButton(
                                    onClick = { onAction(NoteDetailsAction.OnBacK) }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                                        contentDescription = stringResource(R.string.navigate_back),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            }
                        )
                    }

                    else -> {
                        NoteMarkTopAppBar(
                            navigationIcon = {
                                IconButton(
                                    onClick = { onAction(NoteDetailsAction.OnCloseClick) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = stringResource(R.string.close_note),
                                    )
                                }
                            },
                            actionContent = {
                                SaveNoteButton(
                                    text = stringResource(R.string.save_note).uppercase(),
                                    onClick = { onAction(NoteDetailsAction.OnSaveClick) }
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NoteDetailsPortraitContent(
            state = state,
            onAction = onAction,
            descriptionFocusRequester = descriptionFocusRequester,
            focusManager = focusManager,
            modifier = Modifier
                .padding(innerPadding)
        )
    }
}