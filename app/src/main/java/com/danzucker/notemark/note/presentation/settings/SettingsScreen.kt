package com.danzucker.notemark.note.presentation.settings

import android.R.attr.titleTextColor
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.danzucker.notemark.R
import com.danzucker.notemark.core.presentation.designsystem.ChevronIcon
import com.danzucker.notemark.core.presentation.designsystem.ClockIcon
import com.danzucker.notemark.core.presentation.designsystem.LogoutIcon
import com.danzucker.notemark.core.presentation.designsystem.SyncDataIcon
import com.danzucker.notemark.core.presentation.designsystem.components.NoteMarkTopAppBar
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.fontSizeMedium16
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType.*
import com.danzucker.notemark.note.components.NoteListAlertDialog
import com.danzucker.notemark.note.presentation.notelist.NoteAction
import com.danzucker.notemark.note.presentation.settings.components.SettingsListItem
import com.danzucker.notemark.note.presentation.settings.components.SyncIntervalDropDownOptionMenu
import org.koin.androidx.compose.koinViewModel


@Composable
fun SettingsRoot(
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SettingsScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is SettingsAction.OnBackClick -> onBackClick()
                is SettingsAction.OnLogoutClick -> onLogoutClick()
                else -> {}
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun SettingsScreen(
    state: SettingsState,
    onAction: (SettingsAction) -> Unit,
) {
    val windowClass = currentWindowAdaptiveInfo().windowSizeClass
    val contentPadding = when (DeviceScreenType.fromWindowSizeClass(windowClass)) {
        MOBILE_PORTRAIT,
        TABLET_PORTRAIT -> 0.dp
        MOBILE_LANDSCAPE,
        TABLET_LANDSCAPE,
        DESKTOP -> 40.dp
    }

    val context = LocalContext.current
    var dropDownOffset by remember {
        mutableStateOf(IntOffset.Zero)
    }

    var syncIntervalTrailingContentWidth by remember {
        mutableStateOf(IntOffset.Zero)
    }

    val configuration = LocalConfiguration.current
    val dropDownMaxHeight = when (DeviceScreenType.fromWindowSizeClass(windowClass)) {
        MOBILE_PORTRAIT,
        TABLET_PORTRAIT -> (configuration.screenHeightDp * 0.3f).dp
        MOBILE_LANDSCAPE,
        TABLET_LANDSCAPE,
        DESKTOP -> (configuration.screenHeightDp * 0.5f).dp
    }


    Scaffold(
        modifier = Modifier
            .padding(start = contentPadding),
        containerColor = MaterialTheme.colorScheme.onPrimary,
        topBar = {
            NoteMarkTopAppBar(
                modifier = Modifier,
                title = stringResource(R.string.settings).uppercase(),
                titleTextSize = fontSizeMedium16,
                titleColor = MaterialTheme.colorScheme.onSurfaceVariant,
                titleTextOffset = IntOffset(x = (-16), y = 0),
                hasInternetConnection = true, // We don't want to show the offline icon for settings, replace with actual logic if needed
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(SettingsAction.OnBackClick)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                            contentDescription = stringResource(R.string.navigate_back),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            SettingsListItem(
                title = stringResource(R.string.sync_interval),
                modifier = Modifier
                    .onSizeChanged {
                        dropDownOffset = IntOffset(
                            x = it.width - syncIntervalTrailingContentWidth.x, // Adjust the x offset based on the width of the trailing content
                            y = it.height + 16.dp.value.toInt() // 16.dp is the vertical padding we added to the column
                        )
                    },
                onItemClick = {
                    onAction(SettingsAction.OnSyncIntervalClick)
                },
                leadingIcon = {
                    Icon(
                        imageVector = ClockIcon,
                        contentDescription = stringResource(R.string.sync_interval),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingContent = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .onSizeChanged {
                                syncIntervalTrailingContentWidth = IntOffset(
                                    x = it.width,
                                    y = 0
                                ) // Capture the width of the trailing content for dropdown positioning
                            }
                    ) {
                        Text(
                            text = state.syncIntervalText.asString(context), // handle the text based on the selected sync interval
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Icon(
                            imageVector = ChevronIcon,
                            contentDescription = stringResource(R.string.sync_interval),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            )

            HorizontalDivider(
                thickness = 0.5.dp,
            )

            SettingsListItem(
                title = stringResource(R.string.sync_data),
                onItemClick = {
                    if (!state.isSyncingData) {
                        onAction(SettingsAction.OnSyncDataClick) // Trigger manual sync only if not already syncing
                    }

                },
                description = "Last sync: ${(state.lastSyncTimestamp).asString()}", // This should be dynamic based on the actual last sync time
                leadingIcon = {
                    if (state.isSyncingData) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(24.dp)
                                .padding(top = 4.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Icon(
                            imageVector = SyncDataIcon,
                            contentDescription = stringResource(R.string.sync_data),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                },
                verticalAlignment = Alignment.Top
            )

            HorizontalDivider(
                thickness = 0.5.dp,
            )

            SettingsListItem(
                title = stringResource(R.string.logout),
                onItemClick = {
                    onAction(SettingsAction.OnLogoutClick)
                },
                titleTextColor = MaterialTheme.colorScheme.error,
                leadingIcon = {
                    Icon(
                        imageVector = LogoutIcon,
                        contentDescription = stringResource(R.string.sync_data),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            )

            if (state.showError && !state.isDeviceConnected) {
                Text(
                    text = stringResource(R.string.offline_logout_message),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (state.showError && state.isDeviceConnected) {
                Text(
                    text = state.errorMessage.asString(context),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }


            // Sync interval dropdown
            if (state.showSyncIntervalDropdown) {
                SyncIntervalDropDownOptionMenu(
                    selectedInterval = state.selectedSyncInterval,
                    onDismiss = {
                        onAction(SettingsAction.OnDismissSyncIntervalDropdown)
                    },
                    onIntervalClick = { interval ->
                        onAction(SettingsAction.OnSyncIntervalItemSelected(syncInterval = interval))
                    },
                    dropDownOffset = dropDownOffset,
                    maxDropDownHeight = dropDownMaxHeight,
                )
            }

            if (state.isSyncingData) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(48.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Logout Confirmation Dialog
            if (state.showLogoutConfirmationDialog) {
                NoteListAlertDialog(
                    title = stringResource(id = R.string.unsync_title),
                    body = stringResource(id = R.string.unsync_message),
                    confirmText = stringResource(R.string.sync_now),
                    dismissText = stringResource(R.string.logout_without_syncing),
                    onConfirmClick = {
                        onAction(SettingsAction.OnSyncAndLogout)
                    },
                    onDismissClick = {
                        onAction(SettingsAction.OnConfirmLogout)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    NoteMarkTheme {
        SettingsScreen(
            state = SettingsState(),
            onAction = {}
        )
    }
}