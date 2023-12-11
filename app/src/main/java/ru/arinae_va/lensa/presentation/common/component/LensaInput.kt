package ru.arinae_va.lensa.presentation.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.modifier.modifierLocalConsumer
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
    value: String,
    placeholder: String = "",
    inputType: KeyboardType = KeyboardType.Text,
    showRequired: Boolean = false,
    acceptRequiredCheck: (String) -> Boolean = { newInput: String -> newInput.isNotEmpty() },
    enabled: Boolean = true,
    readOnly: Boolean = false,
    maxLines: Int = 1,
    minLines: Int = 1,
    singleLine: Boolean = true,
    showLeadingIcon: Boolean = false,
    onLeadingIconClick: () -> Unit = {},
    showTrailingIcon: Boolean = false,
    onTrailingIconClick: () -> Unit = {},
    trailingIconRes: Int = R.drawable.ic_4_star,
    leadingIconRes: Int = R.drawable.ic_4_star,

    ) {
    var input by remember { mutableStateOf(value) }
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
        onValueChange = { newInput: String ->
            input = newInput
            needToDrawRequiredIcon = !acceptRequiredCheck(newInput)
            onValueChanged(input)
        },
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        cursorBrush = SolidColor(
            value = LensaTheme.colors.textColorAccent,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = inputType,
        ),
        textStyle = LensaTheme.typography.hint.copy(color = LensaTheme.colors.textColor),
        decorationBox = @Composable { innerTextField ->
            Box(
                modifier = Modifier
                    .background(
                        color = Color.Transparent,
                        shape = LensaTheme.shapes.noRoundedCornersShape
                    )
                    .border(width = 1.dp, color = LensaTheme.colors.textColor)
                    .padding(
                        horizontal = 12.dp,
                        vertical = 8.dp,
                    )
            ) {
                Row() {
                    LensaIconButton(
                        onClick = { /*TODO*/ },
                        icon = R.drawable.ic_4_star,
                        iconSize = 24.dp,
                    )
                    HSpace(w = 12.dp)
                    Box {
                        if (value.isBlank()) {
                            Text(
                                text = placeholder,
                                style = LensaTheme.typography.text,
                                color = LensaTheme.colors.textColorSecondary,
                            )
                        }
                        innerTextField()
                    }
                    HSpace(w = 12.dp)
                    LensaIconButton(
                        onClick = { /*TODO*/ },
                        icon = R.drawable.ic_4_star,
                        iconSize = 24.dp,
                    )
                }

            }
//            TextFieldDefaults.OutlinedTextFieldDecorationBox(
//                value = input,
//                innerTextField = innerTextField,
//                enabled = true,
//                singleLine = true,
//                visualTransformation = VisualTransformation.None,
//                interactionSource = interactionSource,
//                contentPadding = PaddingValues(
//                    horizontal = 12.dp,
//                    vertical = 8.dp,
//                ),
//                leadingIcon = leadingIcon,
//                trailingIcon = trailingIcon,
//                placeholder = {
//                    Text(
//                        text = placeholder,
//                        style = LensaTheme.typography.hint,
//                        color = LensaTheme.colors.textColorSecondary,
//                    )
//                },
//                border = {
//                    TextFieldDefaults.BorderBox(
//                        enabled = true,
//                        isError = false,
//                        interactionSource = interactionSource,
//                        colors = TextFieldDefaults.outlinedTextFieldColors(
//                            focusedBorderColor = LensaTheme.colors.textColor,
//                            unfocusedBorderColor = LensaTheme.colors.textColor,
//                        ),
//                        shape = LensaTheme.shapes.noRoundedCornersShape,
//                    )
//                }
//            )
        }
    )
}

@Composable
fun LensaDropdownInput() {

}

private const val MULTILINE_INPUT_MAX_LENGTH = 3000
private const val MULTILINE_INPUT_LINES_COUNT = 10

@Composable
fun LensaMultilineInput(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String = "",
    onValueChanged: (String) -> Unit = {},
    maxLength: Int = MULTILINE_INPUT_MAX_LENGTH,
    linesCount: Int = MULTILINE_INPUT_LINES_COUNT
) {
    var inputLength by remember { mutableStateOf(0) }
    var input by remember { mutableStateOf(value) }
    Column(
        horizontalAlignment = Alignment.End,
    ) {
        LensaInput(
            modifier = modifier,
            onValueChanged = {
                if (it.length <= maxLength) {
                    input = it
                    inputLength = it.length
                    onValueChanged(it)
                }
            },
            placeholder = placeholder,
            value = input,
            showTrailingIcon = true,
            onTrailingIconClick = {
                input = ""
            },
            singleLine = false,
            maxLines = linesCount,
            minLines = linesCount,
        )
        VSpace(h = 8.dp)
        Row(
            modifier = Modifier.width(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Символов: $inputLength/$maxLength",
                style = LensaTheme.typography.signature,
                color = LensaTheme.colors.textColorSecondary
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LensaInputPreview() {
    LensaTheme {
        Column {
            LensaInput(
                value = "+7",
                onValueChanged = {}
            )
            VSpace(h = 16.dp)
            LensaInput(onValueChanged = {}, value = "Test", showLeadingIcon = true)
            VSpace(h = 16.dp)
            LensaMultilineInput(
                placeholder = "Test", value = ""
            )
        }
    }
}