@file:OptIn(ExperimentalMaterial3Api::class)

package com.danzucker.notemark.note.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.danzucker.notemark.R

@Composable
fun NoteListAlertDialog(
    title: String,
    body: String,
    modifier: Modifier = Modifier,
    confirmText: String = stringResource(R.string.ok),
    dismissText: String = stringResource(R.string.cancel),
    onDismissClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {}
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissClick,
        confirmButton = {
            TextButton(
                onClick = onConfirmClick,
            ) {
                Text(
                    text = confirmText,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissClick,
            ) {
                Text(text = dismissText)
            }
        },
        title = {
            Text(
                text = title
            )
        },
        text = {
            Text(
                text = body
            )
        }
    )
}