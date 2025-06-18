package com.danzucker.notemark.core.presentation.designsystem.background

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.cornerRadiusMediumLarge20
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingExtraLarge32
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingExtraLarge40
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingMedium16


@Composable
fun NoteMarkBackground(
    modifier: Modifier = Modifier,
    topStartCornerRadius: Dp = cornerRadiusMediumLarge20,
    topEndCornerRadius: Dp = cornerRadiusMediumLarge20,
    bottomStartCornerRadius: Dp = 0.dp,
    bottomEndCornerRadius: Dp = 0.dp,
    topPadding: Dp = paddingExtraLarge32,
    bottomPadding: Dp = paddingExtraLarge40,
    horizontalStartPadding: Dp = paddingMedium16,
    horizontalEndPadding: Dp = paddingMedium16,
    centerContent: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.onPrimary,
        shape = RoundedCornerShape(
            topStart = topStartCornerRadius,
            topEnd = topEndCornerRadius,
            bottomStart = bottomStartCornerRadius,
            bottomEnd = bottomEndCornerRadius
        ),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = topPadding,
                    start = horizontalStartPadding,
                    end = horizontalEndPadding,
                    bottom = bottomPadding
                ),
            horizontalAlignment = if (centerContent) {
                Alignment.CenterHorizontally
            } else {
                Alignment.Start
            }
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun NoteMarkBackgroundPreview() {
    NoteMarkTheme {
        NoteMarkBackground(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = androidx.compose.ui.graphics.Color.DarkGray
                )
        ) {
            Text(
                text = "Hello world!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Hello world!",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}