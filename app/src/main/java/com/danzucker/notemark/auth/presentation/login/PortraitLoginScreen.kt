package com.danzucker.notemark.auth.presentation.login

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.danzucker.notemark.R
import com.danzucker.notemark.core.presentation.designsystem.background.NoteMarkBackground
import com.danzucker.notemark.core.presentation.designsystem.buttons.PrimaryButton
import com.danzucker.notemark.core.presentation.designsystem.components.NoteMarkHeaderSection
import com.danzucker.notemark.core.presentation.designsystem.components.NoteMarkLink
import com.danzucker.notemark.core.presentation.designsystem.textfields.NoteMarkTextField
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingExtraLarge40
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingLarge24
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingMedium16


@Composable
fun PortraitLoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets.statusBars,
    ) { innerPadding ->
        PortraitLoginContent(
            state = state,
            onAction = onAction,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = paddingMedium16)
                .consumeWindowInsets(WindowInsets.navigationBars)
        )
    }
}


@Composable
fun PortraitLoginContent(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val (_, passwordFocus) = remember { FocusRequester.createRefs() }

    NoteMarkBackground(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {

        NoteMarkHeaderSection(
            title = stringResource(R.string.login),
            subtitle = stringResource(R.string.landing_description),
        )

        Spacer(modifier = Modifier.height(paddingExtraLarge40))

        NoteMarkTextField(
            text = state.email,
            onValueChange = {
                onAction(LoginAction.OnEmailTextChange(it))
            },
            modifier = Modifier,
            label = stringResource(R.string.email),
            placeholder = stringResource(R.string.email_placeholder),
            isError = false, // we don't want to show error for login
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            onImeAction = {
                passwordFocus.requestFocus()
            }
        )

        Spacer(modifier = Modifier.height(paddingMedium16))

        NoteMarkTextField(
            text = state.password,
            onValueChange = {
                onAction(LoginAction.OnPasswordTextChange(it))
            },
            modifier = Modifier.focusRequester(passwordFocus),
            label = stringResource(R.string.password),
            isPassword = true,
            placeholder = stringResource(R.string.password_placeholder),
            isError = false,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            onImeAction = {
                onAction(LoginAction.OnLoginClick)
            }
        )

        Spacer(modifier = Modifier.height(paddingLarge24))

        PrimaryButton(
            text = stringResource(R.string.login),
            onClick = {
                onAction(LoginAction.OnLoginClick)
            },
            isLoading = state.isLoggingIn,
            enabled = state.canLogin
        )

        Spacer(modifier = Modifier.height(paddingLarge24))

        NoteMarkLink(
            text = stringResource(R.string.dont_have_an_account),
            onClick = {
                onAction(LoginAction.OnRegisterTextClick)
            },
        )
    }
}

@Preview
@Composable
private fun PortraitLoginScreenPreview() {
    NoteMarkTheme {
        PortraitLoginScreen(
            state = LoginState(),
            onAction = {}
        )
    }
}