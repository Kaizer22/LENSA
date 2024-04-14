package ru.arinae_va.lensa.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

data class LensaShapes(
    val round40Dp: RoundedCornerShape = RoundedCornerShape(40.dp),
    val roundShape: RoundedCornerShape = RoundedCornerShape(percent = 50),
    val noRoundedCornersShape: RoundedCornerShape = RoundedCornerShape(0.dp),
)

internal val LocalLensaShapes = staticCompositionLocalOf { LensaShapes() }
