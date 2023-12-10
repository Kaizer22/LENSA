package ru.arinae_va.lensa.presentation.common.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.LocalMinimumTouchTargetEnforcement
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LensaButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    shape: Shape = LensaTheme.shapes.noRoundedCornersShape,
    borderStroke: BorderStroke? = BorderStroke(
        width = 1.dp,
        color = LensaTheme.colors.textColor,
    ),
    buttonColors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = LensaTheme.colors.defaultButtonBg,
        contentColor = LensaTheme.colors.textColor,
        disabledBackgroundColor = LensaTheme.colors.defaultButtonBg.copy(alpha = 0.5f),
        disabledContentColor = LensaTheme.colors.textColor.copy(alpha = 0.5f)
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
    CompositionLocalProvider(
        LocalMinimumInteractiveComponentEnforcement provides false,
    ) {
        Button(
            modifier = modifier
                .background(color = Color.Green)
                .defaultMinSize(1.dp, 1.dp),
            onClick = onClick,
            enabled = enabled,
            shape = shape,
            colors = buttonColors,
            contentPadding = contentPadding,
            border = borderStroke,
            elevation = elevation
        ) {
            Row(
                modifier = if (isFillMaxWidth) Modifier.fillMaxWidth() else Modifier.background(color=Color.Red),
                horizontalArrangement = contentArrangement,
                verticalAlignment = contentAlignment,
            ) {
                Text(
                    modifier = Modifier.padding(0.dp),
                    text = text,
                    style = textStyle,
                    color = textColor,
                )
                iconLeftPainter?.let {
                    Spacer(modifier = Modifier.width(iconPadding))
                    Icon(
                        painter = it,
                        contentDescription = null,
                        modifier = Modifier
                            .height(iconHeight)
                            .width(iconWidth)
                    )
                }
            }
        }
    }
}

@Composable
fun LensaIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    @DrawableRes icon: Int,
    iconSize: Dp,
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
    val iconSize = when (type) {
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
    onClick: () -> Unit,
    type: LensaTextButtonType,
    isFillMaxWidth: Boolean = false,
) {
    LensaButton(
        text = text,
        onClick = onClick,
        borderStroke = null,
        isFillMaxWidth = isFillMaxWidth,
        buttonColors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
            contentColor = LensaTheme.colors.textColor,
        ),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
        ),
        contentPadding = PaddingValues(0.dp),
        textStyle = when (type) {
            LensaTextButtonType.ACCENT -> LensaTheme.typography.accentTextButton
            LensaTextButtonType.DEFAULT -> LensaTheme.typography.textButtonDefault
            LensaTextButtonType.SECONDARY -> LensaTheme.typography.signature
        },
        textColor = when (type) {
            LensaTextButtonType.ACCENT -> LensaTheme.colors.textColorAccent
            LensaTextButtonType.DEFAULT -> LensaTheme.colors.textColor
            LensaTextButtonType.SECONDARY -> LensaTheme.colors.textColorSecondary
        },
    )
}

enum class LensaTextButtonType {
    ACCENT, SECONDARY, DEFAULT
}

@Composable
fun LensaStateButton(
    defaultEnabled: Boolean = false,
    onClick: (Boolean) -> Unit,
    iconSize: Dp = 24.dp,
    @DrawableRes iconEnabledRes: Int,
    @DrawableRes iconDisabledRes: Int,
) {
    var isEnabled by remember { mutableStateOf(defaultEnabled) }
    LensaIconButton(
        onClick = {
            isEnabled != isEnabled
            onClick(isEnabled)
        },
        icon = if (isEnabled) iconEnabledRes else iconDisabledRes,
        iconSize = iconSize,
    )
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
            VSpace(h = 16.dp)
            LensaIconButton(
                iconSize = 60.dp,
                icon = R.drawable.ic_arrow_forward,
                onClick = {}
            )
            VSpace(h = 16.dp)
            LensaIconButton(
                iconSize = 24.dp,
                icon = R.drawable.ic_arrow_forward,
                onClick = {}
            )
            VSpace(h = 16.dp)
            LensaButtonWithIcon(
                isFillMaxWidth = true,
                icon = R.drawable.ic_arrow_forward_2,
                text = "КНОПКА\nСТРЕЛКА ",
                onClick = {},
            )
            VSpace(h = 16.dp)
            LensaButtonWithIcon(
                isFillMaxWidth = true,
                icon = R.drawable.ic_arrow_bottom,
                text = "КНОПКА СТРЕЛКА",
                onClick = {},
                type = ButtonWithIconType.MEDIUM,
            )
            VSpace(h = 16.dp)
            LensaTextButton(
                text = "Текстовая кнопка",
                type = LensaTextButtonType.ACCENT,
                onClick = {}
            )
            VSpace(h = 16.dp)
            LensaTextButton(
                text = "Текстовая кнопка",
                type = LensaTextButtonType.DEFAULT,
                onClick = {}
            )
            VSpace(h = 16.dp)
            LensaTextButton(
                text = "Текстовая кнопка",
                type = LensaTextButtonType.SECONDARY,
                onClick = {},
            )
        }
    }
}