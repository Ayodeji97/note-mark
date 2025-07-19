@file:OptIn(ExperimentalTime::class)

package com.danzucker.notemark.note.presentation.notedetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.notemark.R
import com.danzucker.notemark.core.presentation.designsystem.textfields.TransparentTextField
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.core.presentation.util.negativePadding
import com.danzucker.notemark.note.presentation.notedetails.components.NoteDetailsMetaData
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Composable
fun NoteDetailsPortraitContent(
    state: NoteDetailsState,
    onAction: (NoteDetailsAction) -> Unit,
    descriptionFocusRequester: FocusRequester,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
) {
    val isEditable = state.isEditMode
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(
                top = 16.dp,
                bottom = 16.dp
            )
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        TransparentTextField(
            text = state.titleText,
            onValueChange = {
                if (isEditable) {
                    onAction(NoteDetailsAction.OnTitleTextChange(it))
                }
            },
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = stringResource(R.string.note_title_placeholder),
            textStyle = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            singleLine = true,
            readOnly = !isEditable, // Make title read-only if not in edit mode
            keyboardOptions = if (isEditable) {
                KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            } else {
                KeyboardOptions.Default
            },
            keyboardActions = if (isEditable) {
                KeyboardActions(
                    onNext = {
                        descriptionFocusRequester.requestFocus()
                    }
                )
            } else {
                KeyboardActions.Default
            }
        )

        HorizontalDivider(
            thickness = 0.5.dp,
            modifier = Modifier.negativePadding(horizontal = 16.dp)
        )

        if (state.isViewMode || state.isReaderMode) {
            NoteDetailsMetaData(
                dateCreatedTimeText = state.formattedCreatedAt,
                lastEditedTimeText = state.formattedLastEditAt
            )

            HorizontalDivider(
                thickness = 0.5.dp,
                modifier = Modifier.negativePadding(horizontal = 16.dp)
            )
        }

        TransparentTextField(
            text = state.contentText,
            onValueChange = {
                if (isEditable) {
                    onAction(NoteDetailsAction.OnContentTextChange(it))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(descriptionFocusRequester),
            placeholder = stringResource(R.string.note_description_placeholder),
            textStyle = MaterialTheme.typography.bodySmall,
            readOnly = !isEditable, // Make content read-only if not in edit mode
            keyboardOptions = if (isEditable) {
                KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                )
            } else {
                KeyboardOptions.Default
            },
            keyboardActions = if (isEditable) {
                KeyboardActions(
                    onDone = {
                        onAction(NoteDetailsAction.OnSaveClick)
                        focusManager.clearFocus()
                    }
                )
            } else {
                KeyboardActions.Default
            }
        )
    }
}


@Preview
@Composable
private fun NoteDetailsPortraitContentPreview() {
    NoteMarkTheme {
        val descriptionFocusRequester = remember {
            FocusRequester()
        }
        val focusManager = LocalFocusManager.current
        NoteDetailsPortraitContent(
            state = NoteDetailsState(
                titleText = "Sample Note Title",
                contentText = "This is a sample note content. You can edit this text to see how it looks in the note details screen.",
                createdAt = Instant.parse("2023-10-01T12:00:00Z"),
                lastEditAt = Instant.parse("2023-10-01T12:30:00Z")
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