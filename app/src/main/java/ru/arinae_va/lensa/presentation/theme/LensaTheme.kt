package ru.arinae_va.lensa.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

@Composable
fun LensaTheme(
    darkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkMode) lensaDarkColors() else lensaLightColors()

    val materialDarkColorPalette = darkColors()

    val materialColorPalette = lightColors()
    val materialColors = if (darkMode) {
        materialDarkColorPalette
    } else {
        materialColorPalette
    }
    CompositionLocalProvider(
        LocalLensaColors provides colors
    ) {
        MaterialTheme(
            colors = materialColors,
            content = content,
        )
    }

}

internal val LocalLensaColors = staticCompositionLocalOf<LensaColors> {
    error("CompositionLocal LocalZaborColors not present")
}

object LensaTheme {
    val colors: LensaColors
        @Composable
        @ReadOnlyComposable
        get() = LocalLensaColors.current

//    val buttonsColors: LensaButtonColors
//        @Composable
//        @ReadOnlyComposable
//        get() = LocalLensaButtonColors.current

//    val shapes: LensaShapes
//        @Composable
//        @ReadOnlyComposable
//        get() = LocalLensaShapes.current

    val typography: LensaTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalLensaTypography.current
}