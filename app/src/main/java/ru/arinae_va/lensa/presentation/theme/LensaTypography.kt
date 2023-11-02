package ru.arinae_va.lensa.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.arinae_va.lensa.R

data class LensaTypography(
    val logoBig: TextStyle = TextStyle(
        fontSize = 96.sp,
        fontFamily = AmbidexterFontFamily,
        fontWeight = FontWeight.W400,
    ),
    val logoSmall: TextStyle = TextStyle(
        fontSize = 48.sp,
        fontFamily = AmbidexterFontFamily,
        fontWeight = FontWeight.W400,
    )
)

val AmbidexterFontFamily = FontFamily(
    Font(R.font.ambidexter_regular, FontWeight.W400)
)

internal val LocalLensaTypography = staticCompositionLocalOf { LensaTypography() }