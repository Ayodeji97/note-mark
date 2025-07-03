package com.danzucker.notemark.core.presentation.designsystem.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val BrandDefaultShadowColor = Color(0xFF1B1B1C) // Base color used in your design (black-ish)

fun Modifier.defaultShadow(
    shape: Shape = CircleShape,
    elevation: Dp = 8.dp,
    alpha: Float = 0.12f  // #1F = ~12% opacity
): Modifier {
    return this.shadow(
        elevation = elevation,
        shape = shape,
        ambientColor = BrandDefaultShadowColor.copy(alpha = alpha),
        spotColor = BrandDefaultShadowColor.copy(alpha = alpha),
        clip = false
    )
}