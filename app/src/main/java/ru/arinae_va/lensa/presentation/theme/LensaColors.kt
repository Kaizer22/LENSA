package ru.arinae_va.lensa.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val black = Color(0xFF131513)
val white = Color(0xFFEEEEEC)
val gray = Color(0xFF929091)
val purple = Color(0xFFA94FFF)

data class LensaColors(
    val logoColor: Color,
    val backgroundColor: Color,
)

@Composable
fun lensaLightColors() = LensaColors(
    logoColor = black,
    backgroundColor = white,
)

@Composable
fun lensaDarkColors() = LensaColors(
    logoColor = white,
    backgroundColor = black,
)