package com.danzucker.notemark.auth.presentation.register

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType
import com.danzucker.notemark.core.presentation.util.screensize.DeviceScreenType.Companion.fromWindowSizeClass

@Composable
fun RegisterRoot(
    viewModel: RegisterViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

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
            PortraitRegisterScreen()
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