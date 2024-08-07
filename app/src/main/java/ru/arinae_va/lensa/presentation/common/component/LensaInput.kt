package ru.arinae_va.lensa.presentation.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    onFocusChanged: (isFocused: Boolean) -> Unit = {},
    readOnly: Boolean = false,
    maxLength: Int = Int.MAX_VALUE,
    maxLines: Int = 1,
    minLines: Int = 1,
    singleLine: Boolean = true,
    isRoundedShape: Boolean = false,
    isFillMaxWidth: Boolean = true,
    showLeadingIcon: Boolean = false,
    onLeadingIconClick: () -> Unit = {},
    showTrailingIcon: Boolean = false,
    onTrailingIconClick: () -> Unit = {},
    trailingIconRes: Int = R.drawable.ic_4_star,
    trailingIconTint: Color = LensaTheme.colors.textColor,
    leadingIconRes: Int = R.drawable.ic_4_star,
    leadingIconTint: Color = LensaTheme.colors.textColor,
    focusRequester: FocusRequester = remember { FocusRequester() },
) {
    var input by remember(value) { mutableStateOf(value) }
    //val interactionSource = remember { MutableInteractionSource() }

    val needToDrawRequiredIcon = showRequired && input.isEmpty()
//
//    val trailingIcon: @Composable (() -> Unit)? = if (showTrailingIcon || needToDrawRequiredIcon) {
//        {
//            val iconRes = if (needToDrawRequiredIcon) R.drawable.ic_4_star else trailingIconRes
//            LensaIconButton(
//                onClick = onTrailingIconClick,
//                icon = iconRes,
//                iconSize = 24.dp,
//            )
//        }
//    } else null
//    val leadingIcon: @Composable (() -> Unit)? = if (showLeadingIcon) {
//        {
//            LensaIconButton(
//                onClick = onLeadingIconClick,
//                icon = leadingIconRes,
//                iconSize = 24.dp,
//            )
//        }
//    } else null
    BasicTextField(
        modifier = modifier
            .onFocusChanged { focusState ->
                onFocusChanged(focusState.isFocused)
            }
            .focusRequester(focusRequester),
        value = input,
        enabled = enabled,
        readOnly = readOnly,
        onValueChange = { newInput: String ->
            input = newInput.take(maxLength)
            //needToDrawRequiredIcon = showRequired && !acceptRequiredCheck(newInput)
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
                    .border(
                        width = 1.dp,
                        color = LensaTheme.colors.textColor,
                        shape = if (isRoundedShape) LensaTheme.shapes.round40Dp
                        else LensaTheme.shapes.noRoundedCornersShape,
                    )
                    .padding(
                        horizontal = 12.dp,
                        vertical = 8.dp,
                    )
            ) {
                Row {
                    if (showLeadingIcon) {
                        LensaIconButton(
                            onClick = onLeadingIconClick,
                            icon = leadingIconRes,
                            iconTint = leadingIconTint,
                            iconSize = 24.dp,
                        )
                        HSpace(w = 12.dp)
                    }
                    Box(
                        modifier = if (isFillMaxWidth) Modifier.weight(1f)
                        else Modifier
                    ) {
                        if (input.isBlank()) {
                            Text(
                                text = placeholder,
                                style = LensaTheme.typography.text,
                                color = LensaTheme.colors.textColorSecondary,
                            )
                        }
                        innerTextField()
                    }
                    if (showTrailingIcon || needToDrawRequiredIcon) {
                        val iconRes = if (needToDrawRequiredIcon)
                            R.drawable.ic_4_star
                        else
                            trailingIconRes
                        val iconTint = if(needToDrawRequiredIcon)
                            LensaTheme.colors.textColorAccent
                        else
                            trailingIconTint
                        HSpace(w = 12.dp)
                        LensaIconButton(
                            onClick = onTrailingIconClick,
                            iconTint = iconTint,
                            icon = iconRes,
                            iconSize = 24.dp,
                        )
                    }
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

private const val MULTILINE_INPUT_MAX_LENGTH = 3000
private const val MULTILINE_INPUT_LINES_COUNT = 10

@Composable
fun LensaMultilineInput(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String = "",
    onValueChanged: (String) -> Unit = {},
    maxLength: Int = MULTILINE_INPUT_MAX_LENGTH,
    linesCount: Int = MULTILINE_INPUT_LINES_COUNT,
) {
    var inputLength by remember(value) { mutableStateOf(value.length) }
    var input by remember(value) { mutableStateOf(value) }
    Column(
        horizontalAlignment = Alignment.End,
    ) {
        LensaInput(
            modifier = modifier,
            onValueChanged = {
                inputLength = it.length
                onValueChanged(it)
            },
            placeholder = placeholder,
            value = input,
            showTrailingIcon = true,
            trailingIconRes = R.drawable.ic_cancel,
            onTrailingIconClick = {
                input = ""
                inputLength = 0
            },
            singleLine = false,
            maxLines = linesCount,
            minLines = linesCount,
            maxLength = maxLength,
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
            LensaInput(
                onValueChanged = {},
                value = "Test",
                showLeadingIcon = true
            )
            VSpace(h = 16.dp)
            LensaInput(
                onValueChanged = {},
                value = "Test",
                showLeadingIcon = true,
                showTrailingIcon = true,
            )
            VSpace(h = 16.dp)
            LensaInput(
                onValueChanged = {},
                value = "",
                placeholder = "Стилист по маникюру",
                showLeadingIcon = true,
                leadingIconRes = R.drawable.ic_loupe,
                isRoundedShape = true,
            )
            VSpace(h = 16.dp)
            LensaMultilineInput(
                placeholder = "Test", value = ""
            )
            VSpace(h = 16.dp)
        }
    }
}