package ru.arinae_va.lensa.presentation.common.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun LabeledField(
    labelText: String,
    labelStyle: TextStyle,
    text: String,
    textStyle: TextStyle,
    separator: String,
    separatorStyle: TextStyle,
) {
    Row {
        Text(
            text = labelText,
            style = labelStyle,
            color = LensaTheme.colors.textColor,
        )
        Text(
            text = separator,
            style = separatorStyle,
            color = LensaTheme.colors.textColor,
        )
        Text(
            text = text,
            style = textStyle,
            color = LensaTheme.colors.textColor,
        )
    }
}