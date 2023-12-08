package ru.arinae_va.lensa.presentation.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun LensaHeader() {
    LensaLogo(
        modifier = Modifier.padding(16.dp)
    )
    Divider(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(1.dp)
            .background(LensaTheme.colors.dividerColor)
    )
}