package com.danzucker.notemark.auth.presentation.landing

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.danzucker.notemark.R
import com.danzucker.notemark.core.presentation.designsystem.background.NoteMarkBackground
import com.danzucker.notemark.core.presentation.designsystem.buttons.PrimaryButton
import com.danzucker.notemark.core.presentation.designsystem.buttons.SecondaryButton
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingExtraLarge40
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingExtraLarge48
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingExtraLarge60
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingSmall6
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingSmallMedium12


@Composable
fun TabletPortraitLandingScreen(
    onGetStartedClick: () -> Unit,
    onLoginInClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.tablet_landing_image_bg),
            contentDescription = stringResource(R.string.landing_image_bg_content_description),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = paddingExtraLarge60)
        ) {
            NoteMarkBackground(
                topPadding = paddingExtraLarge40,
                horizontalStartPadding = paddingExtraLarge40,
                horizontalEndPadding = paddingExtraLarge40,
                bottomPadding = paddingExtraLarge48,
            ) {
                Text(
                    text = stringResource(R.string.landing_title_tablet),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(paddingSmall6))

                Text(
                    text = stringResource(R.string.landing_description),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(paddingExtraLarge40))

                PrimaryButton(
                    text = stringResource(R.string.get_started),
                    onClick = onGetStartedClick,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(paddingSmallMedium12))

                SecondaryButton(
                    text = stringResource(R.string.login),
                    onClick = onLoginInClick,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
    
}


@Preview
@Composable
private fun TabletPortraitLandingScreenPreview() {
    NoteMarkTheme {
        TabletPortraitLandingScreen(
            onGetStartedClick = {},
            onLoginInClick = {},
            modifier = Modifier
        )
    }
}