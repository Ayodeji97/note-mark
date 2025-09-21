package com.danzucker.notemark.note.presentation.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.note.presentation.settings.util.SyncIntervalUi

@Composable
fun SyncIntervalDropDownOptionMenu(
    selectedInterval: SyncIntervalUi,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    onIntervalClick: (SyncIntervalUi) -> Unit,
    dropDownOffset: IntOffset = IntOffset.Zero,
    maxDropDownHeight: Dp = Dp.Unspecified
) {
    Popup(
        onDismissRequest = onDismiss,
        offset = dropDownOffset
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            shape = RoundedCornerShape(10.dp),
            shadowElevation = 4.dp,
            modifier = modifier
                .heightIn(max = maxDropDownHeight)
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                SyncIntervalUi.entries.forEach { interval ->
                    SyncIntervalDropDownItem(
                        syncInterval = interval,
                        selected = interval == selectedInterval,
                        onIntervalClick = onIntervalClick,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SyncIntervalDropDownOptionMenuPreview() {
    NoteMarkTheme {
        SyncIntervalDropDownOptionMenu(
            selectedInterval = SyncIntervalUi.MANUAL,
            onDismiss = {},
            onIntervalClick = {},
            dropDownOffset = IntOffset(0, 0),
            maxDropDownHeight = 300.dp
        )
    }
}