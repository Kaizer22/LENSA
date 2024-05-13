package ru.arinae_va.lensa.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

data class LensaShapes(
    val round40Dp: RoundedCornerShape = RoundedCornerShape(40.dp),
    val roundShape: RoundedCornerShape = RoundedCornerShape(percent = 50),
    val noRoundedCornersShape: RoundedCornerShape = RoundedCornerShape(0.dp),

    val messageShape: RoundedCornerShape = RoundedCornerShape(size = 16.dp),
    val receivedMessageShape: RoundedCornerShape = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = 0.dp,
        bottomEnd = 16.dp,
    ),
    val sentMessageShape: RoundedCornerShape = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = 16.dp,
        bottomEnd = 0.dp,
    )
)

internal val LocalLensaShapes = staticCompositionLocalOf { LensaShapes() }
