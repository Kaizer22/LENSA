package ru.arinae_va.lensa.presentation.common.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LensaInput(
    modifier: Modifier = Modifier,
    onValueChanged: (String) -> Unit,
    defaultValue: String,
    placeholder: String = ""
) {
    var input by remember { mutableStateOf(defaultValue) }
    val interactionSource = remember { MutableInteractionSource() }
    BasicTextField(
        modifier = modifier,
        value = input,
        onValueChange = { newInput ->
            input = newInput
            onValueChanged(input)
        },
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

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LensaInputPreview() {
    LensaTheme {
        Column {
            LensaInput(
                defaultValue = "+7",
                onValueChanged = {}
            )
        }
    }
}