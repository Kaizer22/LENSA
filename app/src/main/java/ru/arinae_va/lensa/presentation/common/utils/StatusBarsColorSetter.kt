package ru.arinae_va.lensa.presentation.common.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun setSystemUiColor(
    color: Color = LensaTheme.colors.backgroundColor,
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = color
    )
}