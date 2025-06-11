package com.danzucker.notemark.core.presentation.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val LightColorScheme = lightColorScheme(
    primary = NoteMarkBlue,
    secondary = NoteMarkBlue10,
    onPrimaryContainer = NoteMarkWhite30,
    onPrimary = NoteMarkWhite30,
    surface = NoteMarkSurface,
    surfaceVariant = NoteMarkWhite12,
    onSurface = NoteMarkOnSurfaceBlack,
    onSurfaceVariant = NoteMarkOnSurfaceVariantBlack,
    inverseSurface = NoteMarkSurfaceVariantBlack12,
    error = NoteMarkError,
    errorContainer = NoteMarkError
)


@Composable
fun NoteMarkTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}