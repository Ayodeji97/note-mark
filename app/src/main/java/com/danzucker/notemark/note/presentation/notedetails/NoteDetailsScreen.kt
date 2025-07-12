package com.danzucker.notemark.note.presentation.notedetails

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.danzucker.notemark.R
import com.danzucker.notemark.core.presentation.designsystem.components.NoteMarkTopAppBar
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.fontSizeMedium16
import com.danzucker.notemark.core.presentation.util.ObserveAsEvents
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType.*
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType.Companion.fromWindowSizeClass
import com.danzucker.notemark.note.components.NoteListAlertDialog
import com.danzucker.notemark.note.components.SaveNoteButton
import com.danzucker.notemark.note.presentation.notedetails.components.NoteMarkDetailsBottomAppBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun NoteDetailsRoot(
    onNavigateBack: () -> Unit,
    viewModel: NoteDetailsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            NoteDetailsEvent.NoteDetailsSuccessfullySaved,
            NoteDetailsEvent.NavigateBack -> onNavigateBack()

            NoteDetailsEvent.FailedToSaveNoteDetails -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_failed_to_save_note),
                    Toast.LENGTH_LONG
                ).show()
                onNavigateBack()
            }
        }
    }

    NoteDetailsScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun NoteDetailsScreen(
    state: NoteDetailsState,
    onAction: (NoteDetailsAction) -> Unit,
    modifier: Modifier = Modifier
) {

    BackHandler(
        enabled = !state.showDiscardConfirmationDialog
    ) {
        onAction(NoteDetailsAction.OnBacK)
    }
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        topBar = {
            if (state.isViewMode) {
                NoteMarkTopAppBar(
                    modifier = Modifier.offset { IntOffset(x = (-16), y = 0) },
                    title = stringResource(R.string.all_notes).uppercase(),
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
            } else {
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
        },
        bottomBar = {
            NoteMarkDetailsBottomAppBar(
                modifier = Modifier
                    .padding(
                        bottom = WindowInsets
                            .navigationBars
                            .asPaddingValues()
                            .calculateBottomPadding()
                    ),
                isEditModeSelected = false,
                isReadModeSelected = false,
                onEditModeClick = {},
                onReadModeClick = {},
            )
        }
    ) { innerPadding ->

        val descriptionFocusRequester = remember {
            FocusRequester()
        }
        val focusManager = LocalFocusManager.current

        val windowClass = currentWindowAdaptiveInfo().windowSizeClass
        when (fromWindowSizeClass(windowSizeClass = windowClass)) {
            MOBILE_PORTRAIT -> NoteDetailsPortraitContent(
                state = state,
                onAction = onAction,
                descriptionFocusRequester = descriptionFocusRequester,
                focusManager = focusManager,
                modifier = Modifier
                    .padding(innerPadding)
            )

            MOBILE_LANDSCAPE -> NoteDetailsLandScapeContent(
                state = state,
                onAction = onAction,
                descriptionFocusRequester = descriptionFocusRequester,
                focusManager = focusManager,
                modifier = Modifier
                    .padding(innerPadding)
            )

            TABLET_PORTRAIT -> Unit
            TABLET_LANDSCAPE -> Unit
            DESKTOP -> Unit
        }

        if (state.showDiscardConfirmationDialog) {
            NoteListAlertDialog(
                title = stringResource(id = R.string.discard_note_confirmation_title),
                body = stringResource(id = R.string.discard_note_confirmation_body),
                confirmText = stringResource(R.string.discard),
                dismissText = stringResource(R.string.keep_editing),
                onDismissClick = {
                    onAction(NoteDetailsAction.OnKeepEditingClick)
                },
                onConfirmClick = {
                    onAction(NoteDetailsAction.OnDiscardNoteDetailsClick)
                }
            )
        }
    }
}


@Preview
@Composable
private fun NoteDetailsScreenPreview() {
    NoteMarkTheme {
        NoteDetailsScreen(
            state = NoteDetailsState(),
            onAction = {}
        )
    }
}