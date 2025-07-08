package com.danzucker.notemark.note.presentation.notedetails.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.notemark.R
import com.danzucker.notemark.core.presentation.designsystem.EditIcon
import com.danzucker.notemark.core.presentation.designsystem.ReadIcon
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.cornerRadiusLarge16



@Composable
fun NoteMarkDetailsBottomAppBar(
    isEditModeSelected: Boolean,
    isReadModeSelected: Boolean,
    modifier: Modifier = Modifier,
    onEditModeClick: () -> Unit = {},
    onReadModeClick: () -> Unit = {},
) {

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize(),
        shape = RoundedCornerShape(cornerRadiusLarge16),
        color = MaterialTheme.colorScheme.surface,
    ) {

        Row(
            modifier = Modifier
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (isEditModeSelected) {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    )
            ) {
                IconButton(
                    onClick = onEditModeClick,
                ) {
                    Icon(
                        imageVector = EditIcon,
                        contentDescription = stringResource(R.string.edit_botton),
                        tint = if (isEditModeSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (isReadModeSelected) {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    )
            ) {
                IconButton(
                    onClick = onReadModeClick,
                ) {
                    Icon(
                        imageVector = ReadIcon,
                        contentDescription = stringResource(R.string.read_botton),
                        tint = if (isReadModeSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun NoteMarkDetailsBottomAppBarPreview() {
    NoteMarkTheme {
        NoteMarkDetailsBottomAppBar(
            isEditModeSelected = false,
            isReadModeSelected = false,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize()
        )
    }
}