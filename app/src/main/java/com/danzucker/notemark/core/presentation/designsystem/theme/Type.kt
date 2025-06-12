package com.danzucker.notemark.core.presentation.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.danzucker.notemark.R


val Inter = FontFamily(
    Font(
        resId = R.font.inter_light_18,
        weight = FontWeight.Light
    ),

    Font(
        resId = R.font.inter_regular_18,
        weight = FontWeight.Normal
    ),

    Font(
        resId = R.font.inter_medium_18,
        weight = FontWeight.Medium
    ),
    Font(
        resId = R.font.inter_bold_18,
        weight = FontWeight.Bold
    ),
    Font(
        resId = R.font.inter_semibold_18,
        weight = FontWeight.SemiBold
    )
)

val SpaceGrotesk = FontFamily(
    Font(
        resId = R.font.space_grotesk_light,
        weight = FontWeight.Light
    ),
    Font(
        resId = R.font.space_grotesk_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.space_grotesk_medium,
        weight = FontWeight.Medium
    ),

    Font(
        resId = R.font.space_grotesk_bold,
        weight = FontWeight.Bold
    ),

    Font(
        resId = R.font.space_grotesk_semi_bold,
        weight = FontWeight.SemiBold
    )
)


// Set of Material typography styles to start with
val Typography = Typography(
    titleMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal, // 400
        fontSize = 17.sp,
        lineHeight = 24.sp,
        letterSpacing = (17 * 0.01).sp,
        fontFeatureSettings = "lnum, pnum",
    ),
    titleLarge = TextStyle(
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.Bold, // 700
        fontSize = 32.sp,
        lineHeight = 36.sp,
        letterSpacing = (32 * 0.01).sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
        fontFeatureSettings = "lnum, pnum"
    ),
    bodyMedium = TextStyle(
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.Medium, // 500
        fontSize = 17.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    ),
    bodySmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal, // 400
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    ),
    labelMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium, // 500
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    ),

    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)