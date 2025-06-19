package com.danzucker.notemark.auth.presentation.register

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.danzucker.notemark.auth.presentation.login.LoginEvent
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.core.presentation.util.ObserveAsEvents
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType.Companion.fromWindowSizeClass
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterRoot(
    viewModel: RegisterViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            RegisterEvent.OnLoginTextClick -> Unit  // navigate to registration screen
        }
    }

    RegisterScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit,
) {

    val windowClass = currentWindowAdaptiveInfo().windowSizeClass
    when (fromWindowSizeClass(windowSizeClass = windowClass)) {
        DeviceScreenType.MOBILE_PORTRAIT -> {
            PortraitRegisterScreen(
                state = state,
                onAction = onAction,
                modifier = Modifier
            )
        }
        DeviceScreenType.MOBILE_LANDSCAPE -> {
            LandscapeRegisterScreen()
        }
        DeviceScreenType.TABLET_PORTRAIT -> {
            TabletRegisterScreen()
        }
        DeviceScreenType.TABLET_LANDSCAPE -> {
            LandscapeRegisterScreen()
        }
        DeviceScreenType.DESKTOP -> {
            TabletRegisterScreen()
        }
    }
}

@Preview
@Composable
private fun Preview() {
    NoteMarkTheme {
        RegisterScreen(
            state = RegisterState(),
            onAction = {}
        )
    }
}