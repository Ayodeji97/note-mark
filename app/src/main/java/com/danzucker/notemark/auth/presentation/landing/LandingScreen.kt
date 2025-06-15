package com.danzucker.notemark.auth.presentation.landing

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType.MOBILE_PORTRAIT
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType.MOBILE_LANDSCAPE
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType.TABLET_LANDSCAPE
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType.TABLET_PORTRAIT
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType.DESKTOP
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType.Companion.fromWindowSizeClass

@Composable
fun LandingScreen(
    onGetStartedClick: () -> Unit,
    onLoginInClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val windowClass = currentWindowAdaptiveInfo().windowSizeClass

    when (fromWindowSizeClass(windowSizeClass = windowClass)) {
        MOBILE_PORTRAIT -> {
            println("MOBILE_PORTRAIT")
            PortraitLandingScreen(
                onGetStartedClick = onGetStartedClick,
                onLoginInClick = onLoginInClick,
                modifier = Modifier
            )
        }

        MOBILE_LANDSCAPE -> {
            println("MOBILE_LANDSCAPE")
            LandscapeLandingScreen(
                onGetStartedClick = onGetStartedClick,
                onLoginInClick = onLoginInClick,
                modifier = Modifier
            )
        }

        TABLET_PORTRAIT -> {
            println("TABLET_PORTRAIT")
            TabletPortraitLandingScreen(
                onGetStartedClick = onGetStartedClick,
                onLoginInClick = onLoginInClick,
                modifier = Modifier
            )
        }
        TABLET_LANDSCAPE -> {
            println("TABLET_LANDSCAPE")
            TabletPortraitLandingScreen(
                onGetStartedClick = onGetStartedClick,
                onLoginInClick = onLoginInClick,
                modifier = Modifier
            )
        }
        DESKTOP -> {
            println("DESKTOP")
            LandscapeLandingScreen(
                onGetStartedClick = onGetStartedClick,
                onLoginInClick = onLoginInClick,
                modifier = Modifier
            )
        }
    }
}


@Preview
@Composable
private fun LandingScreenPreview() {
    NoteMarkTheme {
        LandingScreen(
            onGetStartedClick = {},
            onLoginInClick = {},
            modifier = Modifier
        )
    }
}