package com.danzucker.notemark.note.components

import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.fontSizeMedium16

@Composable
fun SaveNoteButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick
    ) {
        Text(
            text = text,
            modifier = modifier,
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = fontSizeMedium16,
                lineHeight = 24.sp,
                letterSpacing = 1.sp
            ),
            color = MaterialTheme.colorScheme.primary
        )
    }
}


@Preview
@Composable
private fun SaveNoteButtonPreview() {
    NoteMarkTheme {
        SaveNoteButton(
            text = "Save Note",
            onClick = {}
        )
    }
}