package com.danzucker.notemark.note.components

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
import androidx.compose.ui.tooling.preview.Preview
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.cornerRadiusSmallMedium12
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.elevationLarge
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingExtraSmall4
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingMedium16
import com.danzucker.notemark.note.models.NoteUi

@Composable
fun NoteListItem(
    noteUi: NoteUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(cornerRadiusSmallMedium12),
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        shadowElevation = elevationLarge,
        tonalElevation = elevationLarge
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(paddingExtraSmall4)
        ) {

            Text(
                text = noteUi.createdAt,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(paddingExtraSmall4))

            Text(
                text = noteUi.title,
                style = MaterialTheme.typography.titleLarge,
            )

            Text(
                text = noteUi.content,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
private fun NoteCardPreview() {
    NoteMarkTheme {
        NoteListItem(
            noteUi = NoteUi(
                id = 1,
                title = "Sample Note",
                //createdAt = "2023-10-01T12:00:00Z" // I will format this later
                createdAt = "19 APR",
                content = "Augue non mauris ante viverra ut arcu" +
                        " sed ut lectus interdum morbi sed leo" +
                        " purus gravida non id mi augue.",
            ),
            onClick = {}
        )
    }
}