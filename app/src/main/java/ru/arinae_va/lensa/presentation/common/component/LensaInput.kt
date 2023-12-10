package ru.arinae_va.lensa.presentation.common.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.PreferencesSerializer.defaultValue
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LensaInput(
    modifier: Modifier = Modifier,
    onValueChanged: (String) -> Unit,
    defaultValue: String,
    placeholder: String = "",
    inputType: KeyboardType = KeyboardType.Text,
    showRequired: Boolean = false,
    acceptRequiredCheck: (String) -> Boolean = { newInput:String -> newInput.isNotEmpty() },
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    showLeadingIcon: Boolean = false,
    onLeadingIconClick: () -> Unit = {},
    showTrailingIcon: Boolean = false,
    onTrailingIconClick: () -> Unit = {},
    trailingIconRes: Int = R.drawable.ic_4_star,
    leadingIconRes: Int = R.drawable.ic_4_star,

    ) {
    var input by remember { mutableStateOf(defaultValue) }
    val interactionSource = remember { MutableInteractionSource() }

    var needToDrawRequiredIcon by remember { mutableStateOf(showRequired) }

    val trailingIcon: @Composable (() -> Unit)? = if (showTrailingIcon || needToDrawRequiredIcon) {
        {
            val iconRes = if (needToDrawRequiredIcon) R.drawable.ic_4_star else trailingIconRes
            LensaIconButton(
                onClick = onTrailingIconClick,
                icon = iconRes,
                iconSize = 24.dp,
            )
        }
    } else null
    val leadingIcon: @Composable (() -> Unit)? = if (showLeadingIcon) {
        {
            LensaIconButton(
                onClick = onLeadingIconClick,
                icon = leadingIconRes,
                iconSize = 24.dp,
            )
        }
    } else null
    BasicTextField(
        modifier = modifier,
        value = input,
        enabled = enabled,
        readOnly = readOnly,
        onValueChange = { newInput ->
            input = newInput
            needToDrawRequiredIcon = !acceptRequiredCheck(newInput)
            onValueChanged(input)
        },
        singleLine = true,
        cursorBrush = SolidColor(
            value = LensaTheme.colors.textColorAccent,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = inputType,
        ),
        textStyle = LensaTheme.typography.hint.copy(color = LensaTheme.colors.textColor),
        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.OutlinedTextFieldDecorationBox(
                value = input,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                contentPadding = PaddingValues(
                    horizontal = 12.dp,
                    vertical = 8.dp,
                ),
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                placeholder = {
                    Text(
                        text = placeholder,
                        style = LensaTheme.typography.hint,
                        color = LensaTheme.colors.textColorSecondary,
                    )
                },
                border = {
                    TextFieldDefaults.BorderBox(
                        enabled = true,
                        isError = false,
                        interactionSource = interactionSource,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = LensaTheme.colors.textColor,
                            unfocusedBorderColor = LensaTheme.colors.textColor,
                        ),
                        shape = LensaTheme.shapes.noRoundedCornersShape,
                    )
                }
            )
        }
    )
}

@Composable
fun LensaDropdownInput() {

}

@Composable
fun LensaMultilineInput() {

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LensaInputPreview() {
    LensaTheme {
        Column {
            LensaInput(
                defaultValue = "+7",
                onValueChanged = {}
            )
            VSpace(h = 16.dp)
            LensaInput(onValueChanged = {}, defaultValue = "Test", showLeadingIcon = true)
        }
    }
}