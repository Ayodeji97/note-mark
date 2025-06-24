package com.danzucker.notemark.auth.presentation.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
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
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingExtraLarge60
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingLarge24
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingMedium16

@Composable
fun LandscapeRegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets.statusBars,
    ) { innerPadding ->
        LandscapeRegisterContent(
            state = state,
            onAction = onAction,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(WindowInsets.navigationBars)
        )
    }
}

@Composable
fun LandscapeRegisterContent(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    NoteMarkBackground(
        modifier = modifier
            .fillMaxSize(),
        horizontalStartPadding = paddingExtraLarge60,
        horizontalEndPadding = paddingExtraLarge40
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            NoteMarkHeaderSection(
                title = stringResource(R.string.create_account),
                modifier = Modifier
                    .weight(1f)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .imePadding()
            ) {
                NoteMarkTextField(
                    text = state.username,
                    onValueChange = {
                        onAction(RegisterAction.OnUsernameTextChange(it))
                    },
                    modifier = Modifier,
                    label = stringResource(R.string.username),
                    placeholder = stringResource(R.string.username_placeholder),
                    supportingText = stringResource(R.string.username_supporting_text),
                    errorSupportingText = if (state.usernameValidationState.hasLessThanThreeCharacters) {
                        stringResource(R.string.username_error_minimum_characters_supporting_text)
                    } else {
                        stringResource(R.string.username_error_maximum_characters_supporting_text)
                    },
                    isError = !state.usernameValidationState.hasValidCharacters,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    onImeAction = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )

                Spacer(modifier = Modifier.height(paddingMedium16))

                NoteMarkTextField(
                    text = state.email,
                    onValueChange = {
                        onAction(RegisterAction.OnEmailTextChange(it))
                    },
                    modifier = Modifier,
                    label = stringResource(R.string.email),
                    placeholder = stringResource(R.string.email_placeholder),
                    errorSupportingText = stringResource(R.string.email_error_supporting_text),
                    isError = !state.isEmailValid,
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                    onImeAction = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )

                Spacer(modifier = Modifier.height(paddingMedium16))

                NoteMarkTextField(
                    text = state.password,
                    onValueChange = {
                        onAction(RegisterAction.OnPasswordTextChange(it))
                    },
                    modifier = Modifier,
                    isPassword = true,
                    label = stringResource(R.string.password),
                    placeholder = stringResource(R.string.password_placeholder),
                    supportingText = stringResource(R.string.password_supporting_text),
                    errorSupportingText = stringResource(R.string.password_error_supporting_text),
                    isError = !state.passwordValidationState.isValidPassword,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next,
                    onImeAction = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )

                Spacer(modifier = Modifier.height(paddingMedium16))

                NoteMarkTextField(
                    text = state.confirmPassword,
                    onValueChange = {
                        onAction(RegisterAction.OnConfirmPasswordTextChange(it))
                    },
                    modifier = Modifier,
                    label = stringResource(R.string.reset_password),
                    isPassword = true,
                    placeholder = stringResource(R.string.password_placeholder),
                    errorSupportingText = stringResource(R.string.confirm_password_error_supporting_text),
                    isError = !state.passwordValidationState.hasValidConfirmPassword,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                    onImeAction = {
                        onAction(RegisterAction.OnRegisterClick)
                    }
                )

                Spacer(modifier = Modifier.height(paddingLarge24))

                PrimaryButton(
                    text = stringResource(R.string.create_account),
                    onClick = {
                        onAction(RegisterAction.OnRegisterClick)
                    },
                    isLoading = state.isRegistering,
                    enabled = state.canRegister,
                )

                Spacer(modifier = Modifier.height(paddingLarge24))

                NoteMarkLink(
                    text = stringResource(R.string.already_have_account),
                    onClick = {
                        onAction(RegisterAction.OnLoginTextClick)
                    },
                )
            }
        }
    }
}


@Preview
@Composable
private fun LandscapeRegisterScreenPreview() {
    NoteMarkTheme {
        LandscapeRegisterScreen(
            state = RegisterState(),
            onAction = {}
        )
    }
}