package com.danzucker.notemark.auth.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.danzucker.notemark.R
import com.danzucker.notemark.core.presentation.designsystem.background.NoteMarkBackground
import com.danzucker.notemark.core.presentation.designsystem.buttons.PrimaryButton
import com.danzucker.notemark.core.presentation.designsystem.textfields.NoteMarkTextField
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingExtraLarge100
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingExtraLarge32
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingExtraLarge40
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingLarge24
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingSmall6
import com.danzucker.notemark.core.presentation.util.screensize.TabletPortrait


@Composable
fun TabletLoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier
) {

    val (emailFocus, passwordFocus) = remember { FocusRequester.createRefs() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        NoteMarkBackground(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = WindowInsets.statusBars
                        .asPaddingValues()
                        .calculateTopPadding() + paddingExtraLarge32,
                )
                .verticalScroll(rememberScrollState())
                .imePadding(),
            centerContent = true,
            topPadding = paddingExtraLarge100,
            horizontalStartPadding = paddingExtraLarge100,
            horizontalEndPadding = paddingExtraLarge100
        ) {

            Text(
                text = stringResource(R.string.login),
                style = MaterialTheme.typography.titleLarge,
            )

            Spacer(modifier = Modifier.height(paddingSmall6))

            Text(
                text = stringResource(R.string.landing_description),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
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
                isError = false,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                onImeAction = {
                    passwordFocus.requestFocus()
                }
            )

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
                keyboardType = KeyboardType.Email,
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

            Text(
                text = stringResource(R.string.dont_have_an_account),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        onAction(LoginAction.OnRegisterTextClick)
                    }
            )
        }
    }
}


@TabletPortrait
@Composable
private fun TabletLoginScreenPreview() {
    NoteMarkTheme {
        TabletLoginScreen(
            state = LoginState(),
            onAction = {},
            modifier = Modifier
                .fillMaxSize()
        )
    }
}