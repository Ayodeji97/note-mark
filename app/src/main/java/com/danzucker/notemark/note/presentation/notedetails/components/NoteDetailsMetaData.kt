package com.danzucker.notemark.note.presentation.notedetails.components

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.danzucker.notemark.R
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme


// Structure  -> Row -- Column -- Column
@Composable
fun NoteDetailsMetaData(
    dateCreatedTimeText: String,
    lastEditedTimeText: String,
    modifier: Modifier = Modifier
) {
    Row {
        NoteMetaDataDateTime(
            headerText = stringResource(R.string.date_created),
            dateTimeText = dateCreatedTimeText,
            modifier = modifier.weight(1f)
        )

        NoteMetaDataDateTime(
            headerText = stringResource(R.string.date_last_edited),
            dateTimeText = lastEditedTimeText,
            modifier = modifier.weight(1f)
        )
    }
}

@Preview
@Composable
private fun NoteDetailsMetaDataPreview() {
    NoteMarkTheme {
        NoteDetailsMetaData(
            dateCreatedTimeText = "2023-10-01 12:00",
            lastEditedTimeText = "2023-10-02 14:30",
            modifier = Modifier
        )
    }
}