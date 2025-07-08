package com.danzucker.notemark.note.presentation.notedetails.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme

// Structure  -> Row -- Column -- Column
@Composable
fun NoteDetailsMetaData(
    modifier: Modifier = Modifier
) {
    Row {
        NoteMetaDataDateTime(
            headerText = "Date Created",
            dateTimeText = "26 Sep 2024, 18:54",
            modifier = modifier.weight(1f)
        )

        NoteMetaDataDateTime(
            headerText = "Last Edited",
            dateTimeText = "Just Now",
            modifier = modifier.weight(1f)
        )
    }
}

@Preview
@Composable
private fun NoteDetailsMetaDataPreview() {
    NoteMarkTheme {
        NoteDetailsMetaData()
    }
}