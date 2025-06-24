package com.danzucker.notemark.auth.presentation.register

import android.widget.Toast
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
fun RegisterRoot(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: RegisterViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            RegisterEvent.OnLoginTextClick -> onNavigateToLogin()
            is RegisterEvent.Error -> {
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
            RegisterEvent.RegistrationSuccess -> onRegisterSuccess()
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