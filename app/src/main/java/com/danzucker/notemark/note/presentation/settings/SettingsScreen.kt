package com.danzucker.notemark.note.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.danzucker.notemark.core.presentation.util.negativePadding
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType.*
import com.danzucker.notemark.note.presentation.settings.components.SettingsListItem
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
                    ) {
                        Text(
                            text = state.syncIntervalText, // handle the text based on the selected sync interval
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
                    onAction(SettingsAction.OnSyncIntervalClick)
                },
                description = "Last sync: 12 min ago", // This should be dynamic based on the actual last sync time
                leadingIcon = {
                    Icon(
                        imageVector = SyncDataIcon,
                        contentDescription = stringResource(R.string.sync_data),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .padding(top = 4.dp) // Adjust padding to align with text
                    )
                },
                verticalAlignment = Alignment.Top
            )

            HorizontalDivider(
                thickness = 0.5.dp,
            )

            SettingsListItem(
                title = stringResource(R.string.logout),
                onItemClick = {
                    onAction(SettingsAction.OnSyncIntervalClick)
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