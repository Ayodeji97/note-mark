package com.danzucker.notemark.core.presentation.designsystem.textfields

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danzucker.notemark.R
import com.danzucker.notemark.core.presentation.designsystem.EyeClosedIcon
import com.danzucker.notemark.core.presentation.designsystem.EyeOpenedIcon
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.cornerRadiusSmallMedium12
import com.danzucker.notemark.core.presentation.designsystem.values.Dimens.paddingSmall7


@Composable
fun NoteMarkTextField(
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    supportingText: String? = null,
    errorSupportingText: String? = null,
    isPassword: Boolean = false,
    isError: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    singleLine: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: (() -> Unit)? = null,
) {

    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var isFocused by rememberSaveable { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val currentSupportingText =  when {
        isError && !isFocused && text.isNotBlank() -> errorSupportingText ?: stringResource(R.string.error_occurred)
        isFocused -> supportingText
        else -> null
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        if (!label.isNullOrEmpty()) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = paddingSmall7)
            )
        }
        OutlinedTextField(
            value = text,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    isFocused = it.isFocused
                },
            textStyle = MaterialTheme.typography.titleMedium,
            placeholder = {
                if (!placeholder.isNullOrEmpty()) {
                    Text(placeholder)
                }
            },
            supportingText = {
                currentSupportingText?.let { supportingText ->
                    Text(supportingText)
                }
            },
            trailingIcon = {
                if (isPassword) {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) EyeClosedIcon else EyeOpenedIcon,
                            contentDescription = if (passwordVisible) {
                                stringResource(R.string.hide_password)
                            } else {
                                stringResource(R.string.hide_password)
                            },
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            isError = isError && !isFocused && text.isNotBlank(),
            visualTransformation = if (isPassword && !passwordVisible) {
                PasswordVisualTransformation(mask = '*')
            } else {
                VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isPassword) {
                    KeyboardType.Password
                } else {
                    keyboardType
                },
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onAny = {
                    onImeAction?.invoke()
                    if (imeAction == ImeAction.Done) {
                        keyboardController?.hide()
                    }
                }
            ),
            singleLine = singleLine,
            maxLines = maxLines,
            shape = RoundedCornerShape(cornerRadiusSmallMedium12),
            colors = noteMarkTextFieldColors()
        )
    }
}


@Composable
fun noteMarkTextFieldColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        errorTextColor = MaterialTheme.colorScheme.error,
        errorContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        errorPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
        focusedLabelColor = MaterialTheme.colorScheme.onSurface,
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
        cursorColor = MaterialTheme.colorScheme.primary,
        errorCursorColor = MaterialTheme.colorScheme.error,
        unfocusedBorderColor = Color.Transparent,
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        errorBorderColor = MaterialTheme.colorScheme.error,
        focusedSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unfocusedSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
        errorSupportingTextColor = MaterialTheme.colorScheme.error,
    )
}


@Preview
@Composable
fun NoteMarkTextFieldPreview(modifier: Modifier = Modifier) {
    NoteMarkTheme {
        NoteMarkTextField(
            text = "",
            onValueChange = {},
            label = "Label",
            placeholder = "John.doe@example.com",
            isPassword = false,
            isError = true,
            supportingText = "Use between 3 and 20 characters for your username",
            errorSupportingText = "Username must be at least 3 characters",
            modifier = modifier
                .padding(16.dp)
        )
    }
}
