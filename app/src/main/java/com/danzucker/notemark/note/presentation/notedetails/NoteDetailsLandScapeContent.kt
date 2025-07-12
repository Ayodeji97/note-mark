package com.danzucker.notemark.note.presentation.notedetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.danzucker.notemark.note.presentation.notedetails.components.NoteDetailsMetaData

@Composable
fun NoteDetailsLandScapeContent(
    state: NoteDetailsState,
    onAction: (NoteDetailsAction) -> Unit,
    descriptionFocusRequester: FocusRequester,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp), // Increased horizontal padding for better centering
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.6f) // Take 60% of available width
                .verticalScroll(rememberScrollState()), // Allow scrolling if content is long
            horizontalAlignment = Alignment.Start, // Align text fields to start
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TransparentTextField(
                text = state.titleText,
                onValueChange = {
                    onAction(NoteDetailsAction.OnTitleTextChange(it))
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = stringResource(R.string.note_title_placeholder),
                textStyle = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        descriptionFocusRequester.requestFocus()
                    }
                )
            )

            HorizontalDivider(
                thickness = 0.5.dp
            )

            NoteDetailsMetaData()

            HorizontalDivider(
                thickness = 0.5.dp
            )

            TransparentTextField(
                text = state.contentText,
                onValueChange = {
                    onAction(NoteDetailsAction.OnContentTextChange(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(descriptionFocusRequester),
                placeholder = stringResource(R.string.note_description_placeholder),
                textStyle = MaterialTheme.typography.bodySmall,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onAction(NoteDetailsAction.OnSaveClick)
                        focusManager.clearFocus()
                    }
                )
            )
        }
    }
}



@Preview
@Composable
private fun NoteDetailsLandScapeContentPreview() {
    NoteMarkTheme {
        val descriptionFocusRequester = remember {
            FocusRequester()
        }
        val focusManager = LocalFocusManager.current
        NoteDetailsLandScapeContent(
            state = NoteDetailsState(),
            onAction = {},
            modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
            descriptionFocusRequester = descriptionFocusRequester,
            focusManager = focusManager,
        )
    }
}