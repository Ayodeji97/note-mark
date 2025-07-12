package com.danzucker.notemark.core.presentation.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val NoteMarkBlue = Color(0xFF5977F7)
val NoteMarkBlue10 = Color(0x1A5977F7)
val NoteMarkWhite = Color(0xFFFFFFFF)
val NoteMarkWhite12 = Color(0x1FFFFFFF)
val NoteMarkSurface = Color(0xFFEFEFF2)
val NoteMarkOnSurfaceBlack = Color(0xFF1B1B1C)
val NoteMarkOnSurfaceVariantBlack = Color(0xFF535364)
val NoteMarkSurfaceVariantBlack12 = Color(0x1F1B1B1C)
val NoteMarkBackgroundColor = Color(0xFFE0EAFF)
val NoteMarkOnSurfaceLowest = Color(0xFFFFFFFF)
val NoteMarkGradientLightColor = Color(0xFF58A1F8)
val NoteMarkGradientSolidColor = Color(0xFF5A4CF7)
val NoteMarkLightBlueColor10 = Color(0x1A5977F7)

val NoteMarkError = Color(0xFFE1294B)

val NoteMarkSelectedSurface = Color(0xFFE3E8FC)
val NoteMarkSelectedIconBackground = Color(0xFFDDE5FB)



val ColorScheme.FabGradient: Brush
    get() = Brush.linearGradient(
        colors = listOf(
            NoteMarkGradientLightColor,
            NoteMarkGradientSolidColor
        )
    )


data class NoteMarkSelectedStateColorScheme(
    val surface: Color,
    val iconContainer: Color,
    val iconContent: Color
)

val LightSelectedStateColorScheme = NoteMarkSelectedStateColorScheme(
    surface = NoteMarkSelectedSurface, // #E3E8FC
    iconContainer = NoteMarkSelectedIconBackground, // #DDE5FB
    iconContent = NoteMarkWhite // assuming white icons on selection
)