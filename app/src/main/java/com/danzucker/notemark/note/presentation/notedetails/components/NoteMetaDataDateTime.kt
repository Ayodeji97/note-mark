package com.danzucker.notemark.note.presentation.notedetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme

@Composable
fun NoteMetaDataDateTime(
    headerText: String,
    dateTimeText: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = headerText,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = dateTimeText,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview
@Composable
private fun NoteMetaDataDateTimePreview() {
    NoteMarkTheme {
        NoteMetaDataDateTime(
            headerText = "Date Created",
            dateTimeText = "26 Sep 2024, 18:54",
            modifier = Modifier.padding(16.dp)
        )
    }
}