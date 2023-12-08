package ru.arinae_va.lensa.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

data class LensaShapes(
    val defaultButtonShape: RoundedCornerShape = RoundedCornerShape(0.dp),
    val inputShape: RoundedCornerShape = RoundedCornerShape(0.dp)
)

internal val LocalLensaShapes = staticCompositionLocalOf { LensaShapes() }
