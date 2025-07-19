@file:OptIn(ExperimentalTime::class)

package com.danzucker.notemark.note.presentation.notedetails

import android.content.pm.ActivityInfo
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.danzucker.notemark.R
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.core.presentation.util.ObserveAsEvents
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType.*
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType.Companion.fromWindowSizeClass
import com.danzucker.notemark.note.components.NoteListAlertDialog
import com.danzucker.notemark.note.domain.note.model.NoteSaveStatus
import com.danzucker.notemark.note.presentation.notedetails.components.NoteMarkDetailsBottomAppBar
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import org.koin.androidx.compose.koinViewModel
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Composable
fun NoteDetailsRoot(
    onNavigateBack: () -> Unit,
    viewModel: NoteDetailsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val activity = context as ComponentActivity

    ObserveAsEvents(flow = viewModel.events) { event ->
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

            NoteDetailsEvent.RequestLandscapeOrientation -> {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }

            NoteDetailsEvent.ResetOrientation -> {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
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

    val scrollState = rememberScrollState()

    // Plan to move this logic to viewModel to handle scroll state more effectively
    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.isScrollInProgress }
            .distinctUntilChanged()
            .filter { it } // Only trigger when scrolling starts
            .collect {
                if (state.isReaderMode) {
                    onAction(NoteDetailsAction.OnReaderScrollStart)
                }
            }
    }


    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        bottomBar = {
            AnimatedVisibility(
                visible = when {
                    state.isReaderMode -> state.isReaderUiVisible
                    state.isEditMode -> false
                    else -> true
                },
                enter = fadeIn(animationSpec = tween(delayMillis = 300)),
                exit = fadeOut(animationSpec = tween(delayMillis = 300))
            ) {
                NoteMarkDetailsBottomAppBar(
                    modifier = Modifier
                        .padding(
                            bottom = WindowInsets
                                .navigationBars
                                .asPaddingValues()
                                .calculateBottomPadding()
                        ),
                    isEditModeSelected = state.isEditMode,
                    isReadModeSelected = state.isReaderMode,
                    onEditModeClick = {
                        onAction(NoteDetailsAction.OnEditModeClick)
                    },
                    onReadModeClick = {
                        onAction(NoteDetailsAction.OnReaderModeClick)
                    }
                )
            }
        }
    ) { innerPadding ->

        val windowClass = currentWindowAdaptiveInfo().windowSizeClass
        val descriptionFocusRequester = remember {
            FocusRequester()
        }
        val focusManager = LocalFocusManager.current

        val contentModifier = if (state.isReaderMode) {
            Modifier
                .pointerInput(Unit) {
                    detectTapGestures { onAction(NoteDetailsAction.OnReaderScreenTap) }
                }
        } else {
            Modifier
        }

        when (fromWindowSizeClass(windowSizeClass = windowClass)) {
            MOBILE_PORTRAIT -> NoteDetailsPortraitContentRoot(
                state = state,
                onAction = onAction,
                descriptionFocusRequester = descriptionFocusRequester,
                focusManager = focusManager,
                modifier = contentModifier
            )

            MOBILE_LANDSCAPE -> NoteDetailsLandScapeContentRoot(
                state = state,
                onAction = onAction,
                descriptionFocusRequester = descriptionFocusRequester,
                focusManager = focusManager,
                modifier = contentModifier.padding(innerPadding)
            )

            TABLET_PORTRAIT -> NoteDetailsPortraitContentRoot(
                state = state,
                onAction = onAction,
                descriptionFocusRequester = descriptionFocusRequester,
                focusManager = focusManager,
                modifier = contentModifier
            )
            TABLET_LANDSCAPE -> NoteDetailsLandScapeContentRoot(
                state = state,
                onAction = onAction,
                descriptionFocusRequester = descriptionFocusRequester,
                focusManager = focusManager,
                modifier = contentModifier.padding(innerPadding)
            )
            DESKTOP -> NoteDetailsLandScapeContentRoot(
                state = state,
                onAction = onAction,
                descriptionFocusRequester = descriptionFocusRequester,
                focusManager = focusManager,
                modifier = contentModifier.padding(innerPadding)
            )
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
            state = NoteDetailsState(
                titleText = "Sample Note",
                contentText = "This is a sample note content for preview purposes.",
                originalText = "Sample Note",
                originalContext = "This is a sample note content for preview purposes.",
                createdAt = Instant.parse("2023-10-01T12:00:00Z"),
                lastEditAt = Instant.parse("2023-10-01T12:30:00Z"),
                saveStatus = NoteSaveStatus.DRAFT
            ),
            onAction = {}
        )
    }
}