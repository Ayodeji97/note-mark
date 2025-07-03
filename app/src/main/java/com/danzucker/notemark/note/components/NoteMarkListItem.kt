@file:OptIn(ExperimentalFoundationApi::class)

package com.danzucker.notemark.note.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.cornerRadiusSmallMedium12
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.elevationLarge
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingExtraSmall4
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingMedium16
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType
import com.danzucker.notemark.note.domain.note.model.NoteSaveStatus
import com.danzucker.notemark.note.models.NoteUi
import com.danzucker.notemark.note.presentation.util.truncateForPreview
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Composable
fun NoteListItem(
    noteUi: NoteUi,
    deviceScreenType: DeviceScreenType,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        shape = RoundedCornerShape(cornerRadiusSmallMedium12),
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        shadowElevation = elevationLarge,
        tonalElevation = elevationLarge
    ) {
        Column(
            modifier = Modifier
                .padding(paddingMedium16),
            verticalArrangement = Arrangement.spacedBy(paddingExtraSmall4),
        ) {

            Text(
                text = noteUi.formattedCreatedAt,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = noteUi.title,
                style = MaterialTheme.typography.displayMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = noteUi.content.truncateForPreview(deviceScreenType),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalTime::class)
@Preview
@Composable
private fun NoteCardPreview() {
    NoteMarkTheme {
        NoteListItem(
            noteUi = NoteUi(
                id = "1",
                title = "Sample Note",
                //createdAt = "2023-10-01T12:00:00Z" // I will format this later
                createdAt = Instant.parse("2023-10-01T12:00:00Z"),
                content = "Augue non mauris ante viverra ut arcu" +
                        " sed ut lectus interdum morbi sed leo" +
                        " purus gravida non id mi augue.",
                lastEditAt = Instant.parse("2023-10-01T12:00:00Z"),
                saveStatus = NoteSaveStatus.DRAFT
            ),
            onClick = { /* Handle click */ },
            onLongClick = { /* Handle long click */ },
            deviceScreenType = DeviceScreenType.MOBILE_PORTRAIT,
        )
    }
}