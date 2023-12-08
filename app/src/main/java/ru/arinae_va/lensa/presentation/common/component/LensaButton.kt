package ru.arinae_va.lensa.presentation.common.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun LensaButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    shape: Shape = LensaTheme.shapes.defaultButtonShape,
    borderStroke: BorderStroke? = BorderStroke(
        width = 1.dp,
        color = LensaTheme.colors.textColor,
    ),
    buttonColors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = LensaTheme.colors.defaultButtonBg,
        contentColor = LensaTheme.colors.textColor,
    ),
    elevation: ButtonElevation = ButtonDefaults.elevation(),
    textStyle: TextStyle = LensaTheme.typography.textButton,
    textColor: Color = LensaTheme.colors.textColor,
    iconPadding: Dp = 0.dp,
    contentArrangement: Arrangement.Horizontal = Arrangement.Center,
    contentAlignment: Alignment.Vertical = Alignment.CenterVertically,
    iconHeight: Dp = 24.dp,
    iconWidth: Dp = 24.dp,
    isFillMaxWidth: Boolean = false,
    @DrawableRes icon: Int? = null,
) {
    val iconLeftPainter = icon?.let { painterResource(id = it) }
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        colors = buttonColors,
        contentPadding = contentPadding,
        border = borderStroke,
        elevation = elevation
    ) {
        Row(
            modifier = if (isFillMaxWidth) Modifier.fillMaxWidth() else Modifier,
            horizontalArrangement = contentArrangement,
            verticalAlignment = contentAlignment,
        ) {
            Text(
                text = text,
                style = textStyle,
                color = textColor,
            )
            Spacer(modifier = Modifier.width(iconPadding))
            iconLeftPainter?.let {
                Icon(
                    painter = it,
                    contentDescription = null,
                    modifier = Modifier.height(iconHeight).width(iconWidth)
                )
            }
        }
    }
}

@Composable
fun LensaIconButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    iconSize: Dp,
    onClick: () -> Unit
) {
    LensaButton(
        modifier = modifier,
        icon = icon,
        text = "",
        onClick = onClick,
        contentPadding = PaddingValues(0.dp),
        borderStroke = null,
        buttonColors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
            contentColor = LensaTheme.colors.textColor,
        ),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
        ),
        iconWidth = iconSize,
        iconHeight = iconSize,
    )
}

@Composable
fun LensaButtonWithIcon(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    text: String,
    onClick: () -> Unit,
    isFillMaxWidth: Boolean = false,
    type: ButtonWithIconType = ButtonWithIconType.BIG,
) {
    val iconSize = when(type) {
        ButtonWithIconType.BIG -> 28.dp
        ButtonWithIconType.MEDIUM -> 16.dp
    }
    LensaButton(
        modifier = modifier,
        icon = icon,
        text = text,
        onClick = onClick,
        borderStroke = null,
        buttonColors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
            contentColor = LensaTheme.colors.textColor,
        ),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
        ),
        iconPadding = 4.dp,
        contentArrangement = Arrangement.SpaceBetween,
        isFillMaxWidth = isFillMaxWidth,
        textStyle = when (type) {
            ButtonWithIconType.BIG -> LensaTheme.typography.header2
            ButtonWithIconType.MEDIUM -> LensaTheme.typography.header3
        },
        iconHeight = iconSize,
        iconWidth = iconSize,
    )
}

enum class ButtonWithIconType {
    BIG, MEDIUM
}

@Composable
fun LensaTextButton(
    text: String,
    onClick: () -> String,
) {

}

@Composable
fun LensaStateButton() {

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ButtonsPreview() {
    LensaTheme {
        Column {
            LensaButton(
                text = "КНОПКА",
                onClick = {},
            )
            Spacer(modifier = Modifier.height(16.dp))
            LensaIconButton(
                iconSize = 60.dp,
                icon = R.drawable.ic_arrow_forward,
                onClick = {}
            )
            Spacer(modifier = Modifier.height(16.dp))
            LensaButtonWithIcon(
                isFillMaxWidth = true,
                icon = R.drawable.ic_arrow_forward_2,
                text = "КНОПКА\nСТРЕЛКА ",
                onClick = {},
            )
            Spacer(modifier = Modifier.height(16.dp))
            LensaButtonWithIcon(
                isFillMaxWidth = true,
                icon = R.drawable.ic_arrow_bottom,
                text = "КНОПКА СТРЕЛКА",
                onClick = {},
                type = ButtonWithIconType.MEDIUM,
            )
        }

    }
}