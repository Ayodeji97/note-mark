package com.danzucker.notemark.auth.presentation.register

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.statusBars
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
            RegisterEvent.OnLoginTextClick ->{
                println("Navigating to Login Screen")
            }
            is RegisterEvent.Error -> {
                println("Error: ${event.error.asString(context)}")
            }
            RegisterEvent.RegistrationSuccess -> {
                // Handle successful registration, e.g., navigate to the main screen
                println("Registration successful")
            }
        }
    }

    RegisterScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = Modifier
    )
}

@Composable
fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit,
    modifier: Modifier = Modifier
) {

    val windowClass = currentWindowAdaptiveInfo().windowSizeClass
    when (fromWindowSizeClass(windowSizeClass = windowClass)) {
        DeviceScreenType.MOBILE_PORTRAIT -> {
            PortraitRegisterScreen(
                state = state,
                onAction = onAction,
                modifier = modifier
            )
        }
        DeviceScreenType.MOBILE_LANDSCAPE -> {
            LandscapeRegisterScreen(
                state = state,
                onAction = onAction,
                modifier = modifier
            )
        }
        DeviceScreenType.TABLET_PORTRAIT -> {
            TabletRegisterScreen(
                state = state,
                onAction = onAction,
                modifier = modifier
            )
        }
        DeviceScreenType.TABLET_LANDSCAPE -> {
            LandscapeRegisterScreen(
                state = state,
                onAction = onAction,
                modifier = modifier
            )
        }
        DeviceScreenType.DESKTOP -> {
            TabletRegisterScreen(
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
        RegisterScreen(
            state = RegisterState(),
            onAction = {},
            modifier = Modifier
        )
    }
}