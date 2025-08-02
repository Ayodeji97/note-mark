package com.danzucker.notemark.note.presentation.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme

@Composable
fun SettingsListItem(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    titleTextColor: Color = MaterialTheme.colorScheme.onSurface,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    onItemClick: (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .clickable {
                onItemClick?.invoke()
            },
        horizontalArrangement = Arrangement.spacedBy(14.5.dp),
        verticalAlignment = verticalAlignment
    ) {
        leadingIcon?.invoke()
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = titleTextColor
            )
            description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        trailingContent?.invoke()
    }
}


@Preview
@Composable
private fun SettingsListItemPreview() {
    NoteMarkTheme {
        SettingsListItem(
            title = "Settings Item",
            description = "This is a description for the settings item.",
            onItemClick = {},
            leadingIcon = {
                // Replace with an actual icon composable
                Text(text = "Icon")
            },
            trailingContent = {
                // Replace with an actual trailing content composable
                Text(text = "Trailing")
            }
        )
    }
}