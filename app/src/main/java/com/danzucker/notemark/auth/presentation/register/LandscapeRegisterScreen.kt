package com.danzucker.notemark.auth.presentation.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.danzucker.notemark.R
import com.danzucker.notemark.core.presentation.designsystem.background.NoteMarkBackground
import com.danzucker.notemark.core.presentation.designsystem.buttons.PrimaryButton
import com.danzucker.notemark.core.presentation.designsystem.textfields.NoteMarkTextField
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingExtraLarge40
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingExtraLarge60
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingLarge24
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingSmall6
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingSmall8
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingSmallMedium12

@Composable
fun LandscapeRegisterScreen(
    modifier: Modifier = Modifier
) {
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
                        .calculateTopPadding() + paddingSmallMedium12
                ),
            horizontalStartPadding = paddingExtraLarge60,
            horizontalEndPadding = paddingExtraLarge40
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
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
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    NoteMarkTextField(
                        text = "",
                        onValueChange = {},
                        modifier = Modifier,
                        label = stringResource(R.string.username),
                        placeholder = stringResource(R.string.username_placeholder),
                        isError = false, // Change latter
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                        onImeAction = {

                        }
                    )

                    Spacer(modifier = Modifier.height(paddingSmall8))

                    NoteMarkTextField(
                        text = "",
                        onValueChange = {},
                        modifier = Modifier,
                        label = stringResource(R.string.email),
                        placeholder = stringResource(R.string.email_placeholder),
                        isError = false, // Change latter
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                        onImeAction = {

                        }
                    )

                    Spacer(modifier = Modifier.height(paddingSmall8))

                    NoteMarkTextField(
                        text = "",
                        onValueChange = {},
                        modifier = Modifier,
                        isPassword = true,
                        label = stringResource(R.string.password),
                        placeholder = stringResource(R.string.password_placeholder),
                        isError = false, // Change latter
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                        onImeAction = {

                        }
                    )

                    Spacer(modifier = Modifier.height(paddingSmall8))

                    NoteMarkTextField(
                        text = "",
                        onValueChange = {},
                        modifier = Modifier,
                        label = stringResource(R.string.reset_password),
                        isPassword = true,
                        placeholder = stringResource(R.string.password_placeholder),
                        isError = false, // Change latter
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                        onImeAction = {

                        }
                    )

                    Spacer(modifier = Modifier.height(paddingLarge24))

                    PrimaryButton(
                        text = stringResource(R.string.login),
                        onClick = { /* Handle login click */ },
                        isLoading = false,
                        enabled = true, // Change latter
                    )

                    Spacer(modifier = Modifier.height(paddingLarge24))

                    Text(
                        text = stringResource(R.string.already_have_account),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun LandscapeRegisterScreenPreview() {
    NoteMarkTheme {
        LandscapeRegisterScreen()
    }
}