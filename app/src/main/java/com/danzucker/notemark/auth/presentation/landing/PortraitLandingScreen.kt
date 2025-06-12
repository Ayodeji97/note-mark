package com.danzucker.notemark.auth.presentation.landing

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.danzucker.notemark.R
import com.danzucker.notemark.core.presentation.designsystem.background.NoteMarkBackground
import com.danzucker.notemark.core.presentation.designsystem.buttons.PrimaryButton
import com.danzucker.notemark.core.presentation.designsystem.buttons.SecondaryButton
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingExtraLarge40
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingSmall6
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingSmallMedium12


@Composable
fun PortraitLandingRoot(
    onGetStartedClick: () -> Unit,
    onLoginInClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    PortraitLandingScreen(
        onAction = { action ->
            when (action) {
                LandingAction.OnGetStartedClick -> onGetStartedClick()
                LandingAction.OnLoginClick -> onLoginInClick()
            }
        },
        modifier = Modifier
    )
}

@Composable
fun PortraitLandingScreen(
    onAction: (LandingAction) -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.landing_image_bg),
            contentDescription = stringResource(R.string.landing_image_bg_content_description),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            NoteMarkBackground {
                Text(
                    text = stringResource(R.string.landing_title),
                    style = MaterialTheme.typography.titleLarge,
                )

                Spacer(modifier = Modifier.height(paddingSmall6))

                Text(
                    text = stringResource(R.string.landing_description),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(paddingExtraLarge40))

                PrimaryButton(
                    text = stringResource(R.string.get_started),
                    onClick = { onAction(LandingAction.OnGetStartedClick) },
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(paddingSmallMedium12))

                SecondaryButton(
                    text = stringResource(R.string.login),
                    onClick = { onAction(LandingAction.OnLoginClick) },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}


@Preview
@Composable
fun PortraitLandingScreenPreview(
    modifier: Modifier = Modifier
) {
    NoteMarkTheme {
        PortraitLandingScreen(
            onAction = {}
        )
    }
}