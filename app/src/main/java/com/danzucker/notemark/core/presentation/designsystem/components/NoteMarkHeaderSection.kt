package com.danzucker.notemark.core.presentation.designsystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.danzucker.notemark.R
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingSmall6

@Composable
fun NoteMarkHeaderSection(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String = stringResource(R.string.landing_description),
    shouldCenterContent: Boolean = false,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = if (shouldCenterContent) CenterHorizontally else Start
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(paddingSmall6))

        Text(
            text = subtitle,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Preview
@Composable
private fun NoteMarkHeaderSectionPreview() {
    NoteMarkHeaderSection(
        title = stringResource(R.string.login),
        subtitle = stringResource(R.string.landing_description),
        modifier = Modifier,
        shouldCenterContent = true
    )
}