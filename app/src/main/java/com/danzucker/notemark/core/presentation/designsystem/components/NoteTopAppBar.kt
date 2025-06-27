@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.danzucker.notemark.core.presentation.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.fontSizeMediumLarge20

@Composable
fun NoteTopAppBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    actionContent: (@Composable () -> Unit)? = null,
    navigationIcon: (@Composable () -> Unit)? = null,
) {
    TopAppBar(
        title = {
            if (!title.isNullOrEmpty()) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = fontSizeMediumLarge20,
                        letterSpacing = 0.sp,
                        lineHeight = 24.sp
                    )
                )
            }
        },
        modifier = modifier,
        navigationIcon = {
            navigationIcon?.invoke()
        },
        actions = {
            Box(
                modifier = Modifier
                    .padding(end = 16.dp)
            ) {
                actionContent?.invoke()
            }

        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        )
    )
}

@Preview
@Composable
private fun NoteTopAppBarPreview() {
    NoteMarkTheme {
        NoteTopAppBar(
            title = "NoteMark",
            modifier = Modifier,
            actionContent = {

            },
            navigationIcon = {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                        contentDescription = "Go Back",
                    )
                }
            }
        )
    }
}