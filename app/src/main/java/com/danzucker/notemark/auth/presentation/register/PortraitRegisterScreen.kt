package com.danzucker.notemark.auth.presentation.register

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
import androidx.compose.ui.Alignment
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
import com.danzucker.notemark.core.presentation.designsystem.textfields.NoteMarkTextField
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingExtraLarge32
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingExtraLarge40
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingLarge24
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingSmall6
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingSmall8

@Composable
fun PortraitRegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

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
                .imePadding()
        ) {
            Text(
                text = stringResource(R.string.create_account),
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
                text = state.username,
                onValueChange = {
                    onAction(RegisterAction.OnUsernameTextChange(it))
                },
                modifier = Modifier,
                label = stringResource(R.string.username),
                placeholder = stringResource(R.string.username_placeholder),
                supportingText = stringResource(R.string.username_supporting_text),
                errorSupportingText = stringResource(R.string.username_error_supporting_text),
                isError = !state.isUsernameValid,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                onImeAction = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )

            Spacer(modifier = Modifier.height(paddingSmall8))

            NoteMarkTextField(
                text = state.email,
                onValueChange = {
                    onAction(RegisterAction.OnEmailTextChange(it))
                },
                modifier = Modifier,
                label = stringResource(R.string.email),
                placeholder = stringResource(R.string.email_placeholder),
                errorSupportingText = stringResource(R.string.email_error_supporting_text),
                isError = !state.isEmailValid, // Change latter
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                onImeAction = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )

            Spacer(modifier = Modifier.height(paddingSmall8))

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
                isError = !state.isPasswordValid,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                onImeAction = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )

            Spacer(modifier = Modifier.height(paddingSmall8))

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

            Text(
                text = stringResource(R.string.already_have_account),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        onAction(RegisterAction.OnLoginTextClick)
                    }
            )

        }
    }
}


@Preview
@Composable
private fun PortraitLoginScreenPreview() {
    NoteMarkTheme {
        PortraitRegisterScreen(
            state = RegisterState(),
            onAction = {}
        )
    }
}