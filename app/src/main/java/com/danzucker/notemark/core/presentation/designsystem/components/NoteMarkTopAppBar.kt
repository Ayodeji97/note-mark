@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.danzucker.notemark.core.presentation.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danzucker.notemark.R
import com.danzucker.notemark.core.presentation.designsystem.CloudOffIcon
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.fontSizeMediumLarge20

@Composable
fun NoteMarkTopAppBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    onTitleClick: () -> Unit = {},
    hasInternetConnection: Boolean = false,
    titleTextSize: TextUnit = fontSizeMediumLarge20,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    titleTextOffset: IntOffset = IntOffset(0, 0),
    actionContent: (@Composable () -> Unit)? = null,
    navigationIcon: (@Composable () -> Unit)? = null,
) {
    TopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!title.isNullOrEmpty()) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = titleTextSize,
                            letterSpacing = 0.sp,
                            lineHeight = 24.sp
                        ),
                        color = titleColor,
                        modifier = Modifier
                            .offset {
                                titleTextOffset
                            }
                            .clickable(
                                onClick = onTitleClick
                            )
                    )
                }
                if (!hasInternetConnection) {
                    Icon(
                        imageVector = CloudOffIcon,
                        contentDescription = stringResource(R.string.offline),
                        modifier = Modifier
                    )
                }
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
        NoteMarkTopAppBar(
            title = "NoteMark",
            modifier = Modifier,
            hasInternetConnection = false,
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