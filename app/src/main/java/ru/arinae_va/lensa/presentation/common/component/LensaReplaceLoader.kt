package ru.arinae_va.lensa.presentation.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun LensaReplaceLoader(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier.background(
            color = LensaTheme.colors.backgroundColor,
        ),
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "ЗАГРУЗКА...",
                    style = LensaTheme.typography.header2,
                    color = LensaTheme.colors.textColor,
                )
            }
        } else {
            content.invoke()
        }
    }
}