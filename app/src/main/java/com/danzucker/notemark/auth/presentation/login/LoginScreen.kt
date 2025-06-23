package com.danzucker.notemark.auth.presentation.login

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.core.presentation.util.ObserveAsEvents
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType.Companion.fromWindowSizeClass
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginRoot(
    viewModel: LoginViewModel = koinViewModel(),
    modifier: Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            LoginEvent.OnRegisterTextClick -> Unit // navigate to registration screen
            is LoginEvent.Error -> Unit
            LoginEvent.LoginSuccess -> Unit
        }
    }

    LoginScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = Modifier
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier
) {

    val windowClass = currentWindowAdaptiveInfo().windowSizeClass
    when (fromWindowSizeClass(windowSizeClass = windowClass)) {
        DeviceScreenType.MOBILE_PORTRAIT -> {
            PortraitLoginScreen(
                state = state,
                onAction = onAction,
                modifier = modifier
            )
        }

        DeviceScreenType.MOBILE_LANDSCAPE -> {
            LandscapeLoginScreen(
                state = state,
                onAction = onAction,
                modifier = modifier
            )
        }

        DeviceScreenType.TABLET_PORTRAIT -> {
            TabletLoginScreen(
                state = state,
                onAction = onAction,
                modifier = modifier
            )
        }

        DeviceScreenType.TABLET_LANDSCAPE -> {
            LandscapeLoginScreen(
                state = state,
                onAction = onAction,
                modifier = modifier
            )
        }

        DeviceScreenType.DESKTOP -> {
            TabletLoginScreen(
                state = state,
                onAction = onAction,
                modifier = modifier
            )
        }
    }
}


@Preview
@Composable
private fun Preview() {
    NoteMarkTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {}
        )
    }
}