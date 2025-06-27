package com.danzucker.notemark.note.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.notemark.core.presentation.designsystem.theme.FabGradient
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme

@Composable
fun NoteMarkGradientFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .wrapContentSize()
            .background(
                brush = MaterialTheme.colorScheme.FabGradient,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        FloatingActionButton(
            onClick = onClick,
            containerColor = Color.Transparent,
            contentColor = Color.Transparent,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Note",
                tint = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}



@Preview
@Composable
private fun NoteMarkGradientFloatingActionButtonPreview() {
    NoteMarkTheme {
        NoteMarkGradientFloatingActionButton(
            onClick = {},
            modifier = Modifier
                .size(40.dp)
        )
    }
}