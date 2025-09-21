package com.danzucker.notemark.note.presentation.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType.DESKTOP
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType.MOBILE_LANDSCAPE
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType.MOBILE_PORTRAIT
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType.TABLET_LANDSCAPE
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType.TABLET_PORTRAIT
import com.danzucker.notemark.note.presentation.settings.util.SyncIntervalUi

@Composable
fun SyncIntervalDropDownItem(
    syncInterval: SyncIntervalUi,
    selected: Boolean,
    onIntervalClick: (SyncIntervalUi) -> Unit,
    modifier: Modifier = Modifier
) {

    val windowClass = currentWindowAdaptiveInfo().windowSizeClass
    val screenWidthFraction = when (DeviceScreenType.fromWindowSizeClass(windowClass)) {
        MOBILE_PORTRAIT,
        TABLET_PORTRAIT -> 0.4f
        MOBILE_LANDSCAPE,
        TABLET_LANDSCAPE,
        DESKTOP -> 0.2f
    }
    Row(
        modifier = modifier
            .fillMaxWidth(screenWidthFraction)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                onIntervalClick(syncInterval)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = syncInterval.title.asString(),
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = if (selected)
                MaterialTheme.colorScheme.primary else Color.Transparent,
        )
    }
}

@Preview
@Composable
private fun SyncIntervalDropDownMenuPreview() {
    NoteMarkTheme {
        SyncIntervalDropDownItem(
            syncInterval = SyncIntervalUi.MANUAL,
            selected = false,
            onIntervalClick = {}
        )
    }
}