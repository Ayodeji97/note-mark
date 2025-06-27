package com.danzucker.notemark.note.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.fontSizeMediumLarge17

@Composable
fun ProfileInitials(
    profileInitials: String,
    modifier: Modifier = Modifier,
    onProfileClick: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primary)
            .clickable { onProfileClick?.invoke() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = profileInitials,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = fontSizeMediumLarge17,
                letterSpacing = 0.sp,
                lineHeight = 24.sp
            )
        )
    }
}

@Preview
@Composable
private fun ProfileInitialsPreview() {
    NoteMarkTheme {
        ProfileInitials(
            profileInitials = "AB",
            onProfileClick = {}
        )
    }
}