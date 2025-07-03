package com.danzucker.notemark.core.presentation.designsystem.textfields

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.danzucker.notemark.core.presentation.designsystem.theme.NoteMarkTheme



@Composable
fun TransparentTextField(
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    placeholderColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant
    ),
    cursorColor: Color = MaterialTheme.colorScheme.primary,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {

    BasicTextField(
        value = text,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = textStyle,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        cursorBrush = SolidColor(cursorColor),
        singleLine = singleLine,
        maxLines = maxLines,
        decorationBox = { innerTextField ->
            Box(
                contentAlignment = Alignment.CenterStart
            ) {
                if (text.isBlank() && placeholder != null) {
                    Text(
                        text = placeholder,
                        color = placeholderColor,
                        style = textStyle
                    )
                } else {
                    innerTextField()
                }
            }
        }
    )
}


@Preview
@Composable
private fun TransparentTextFieldPreview() {
    NoteMarkTheme {
        TransparentTextField(
            text = "Sample Text",
            onValueChange = {},
            modifier = Modifier,
            placeholder = "Enter text here",
            placeholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            singleLine = true
        )
    }
}
