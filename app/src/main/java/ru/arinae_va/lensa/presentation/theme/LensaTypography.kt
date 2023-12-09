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
    ),
    val header1: TextStyle = TextStyle(
        lineHeight = 57.sp,
        fontSize = 64.sp,
        fontFamily = FigtreeFontFamily,
        fontWeight = FontWeight.W700,
    ),
    val header2: TextStyle = TextStyle(
        fontSize = 40.sp,
        fontFamily = FigtreeFontFamily,
        fontWeight = FontWeight.W700,
    ),
    val header3: TextStyle = TextStyle(
        fontSize = 24.sp,
        fontFamily = FigtreeFontFamily,
        fontWeight = FontWeight.W700,
    ),
    val text: TextStyle = TextStyle(
        fontSize = 20.sp,
        fontFamily = FigtreeFontFamily,
        fontWeight = FontWeight.W400,
    ),
    val linkText: TextStyle = TextStyle(
        fontSize = 20.sp,
        fontFamily = FigtreeFontFamily,
        fontWeight = FontWeight.W400,
    ),
    val accentTextButton: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontFamily = FigtreeFontFamily,
        fontWeight = FontWeight.W400,
    ),
    val smallAccent: TextStyle = TextStyle(
        fontSize = 10.sp,
        fontFamily = FigtreeFontFamily,
        fontWeight = FontWeight.W400,
    ),
    val signature: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontFamily = FigtreeFontFamily,
        fontWeight = FontWeight.W400,
    ),
    val hint: TextStyle = TextStyle(
        fontSize = 20.sp,
        fontFamily = FigtreeFontFamily,
        fontWeight = FontWeight.W400,
    ),
    val name: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontFamily = FigtreeFontFamily,
        fontWeight = FontWeight.W600,
    ),
    val textButton: TextStyle = TextStyle(
        fontSize = 24.sp,
        fontFamily = FigtreeFontFamily,
        fontWeight = FontWeight.W400,
    ),
    val textButtonDefault: TextStyle = TextStyle(
        fontSize = 24.sp,
        fontFamily = FigtreeFontFamily,
        fontWeight = FontWeight.W700,
    ),
)

val AmbidexterFontFamily = FontFamily(
    Font(R.font.ambidexter_regular, FontWeight.W400),
)
val FigtreeFontFamily = FontFamily(
    Font(R.font.figtree_extra_bold, FontWeight.W700),
    Font(R.font.figtree_bold, FontWeight.W600),
    Font(R.font.figtree_regular, FontWeight.W400),
)

internal val LocalLensaTypography = staticCompositionLocalOf { LensaTypography() }