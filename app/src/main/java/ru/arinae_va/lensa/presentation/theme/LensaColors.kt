package ru.arinae_va.lensa.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val black = Color(0xFF131513)
val white = Color(0xFFEEEEEC)
val white_2 = Color(0xFFECF2EF)
val gray_20 = Color(0xFFe7e4e5)
val gray_40 = Color(0xFF929091)
val darkGray = Color(0xFF3d3c3d)
val purple_30 = Color(0xFFff99f5)
val purple_40 = Color(0xFFA94FFF)
val purple_50 = Color(0xFF8d42d4)
val purple_80 = Color(0xFF7134a9)

data class LensaColors(
    val logoColor: Color,
    val fadeColor: Color,
    val dividerColor: Color,
    val backgroundColor: Color,
    val defaultButtonBg: Color,
    val textColor: Color,
    val textColorSecondary: Color,
    val textColorAccent: Color,
    val sentMessageColor: Color,
    val receivedMessage: Color,
)

@Composable
fun lensaLightColors() = LensaColors(
    logoColor = black,
    fadeColor = gray_40.copy(alpha = 0.8f),
    dividerColor = black,
    backgroundColor = white,
    textColor = black,
    textColorSecondary = gray_40,
    textColorAccent = purple_40,
    defaultButtonBg = purple_40,
    sentMessageColor = purple_30,
    receivedMessage = gray_20,
)

@Composable
fun lensaDarkColors() = LensaColors(
    logoColor = white,
    fadeColor = black.copy(alpha = 0.8f),
    dividerColor = white,
    backgroundColor = black,
    textColor = white,
    textColorAccent = purple_40,
    textColorSecondary = gray_20,
    defaultButtonBg = purple_40,
    receivedMessage = darkGray,
    sentMessageColor = purple_80,
)