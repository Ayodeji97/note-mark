package com.danzucker.notemark.note.createnote

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.danzucker.notemark.R
import com.danzucker.notemark.core.presentation.designsystem.components.NoteMarkTopAppBar
import com.danzucker.notemark.core.presentation.designsystem.textfields.TransparentTextField
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingMediumLarge20
import com.danzucker.notemark.note.components.SaveNoteButton

@Composable
fun CreateNoteRoot(
    viewModel: CreateNoteViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    EditNoteScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun EditNoteScreen(
    state: CreateNoteState,
    onAction: (CreateNoteAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            NoteMarkTopAppBar(
              navigationIcon = {
                  IconButton(
                      onClick = {
                            onAction(CreateNoteAction.OnCloseClick)
                      }
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
                        onClick = {
                            onAction(CreateNoteAction.OnSaveClick)
                        }
                    )
                }
            )
        }
    ) { innerPadding ->

        val descriptionFocusRequester = remember {
            FocusRequester()
        }
        val focusManager = LocalFocusManager.current

        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .padding(
                    top = 16.dp,
                    bottom = 16.dp
                )
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            TransparentTextField(
                text = state.titleText,
                onValueChange = {
                    onAction(CreateNoteAction.OnTitleTextChange(it))
                },
                modifier = Modifier
                    .fillMaxWidth(),
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

          //  Spacer(modifier = Modifier.height(paddingMediumLarge20))

            TransparentTextField(
                text = state.contentText,
                onValueChange = {
                    onAction(CreateNoteAction.OnContentTextChange(it))
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
                        focusManager.clearFocus()
                    }
                )
            )
        }
    }
}


@Preview
@Composable
private fun EditNoteScreenPreview() {
    NoteMarkTheme {
        EditNoteScreen(
            state = CreateNoteState(),
            onAction = {}
        )
    }
}