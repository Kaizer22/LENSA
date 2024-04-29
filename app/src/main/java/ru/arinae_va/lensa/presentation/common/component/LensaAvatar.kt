package ru.arinae_va.lensa.presentation.common.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun LensaAvatar(
    avatarUrl: String,
    onClick: () -> Unit = {},
){
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(shape = LensaTheme.shapes.roundShape)
            .clickable(onClick = onClick),
    ) {
        LensaAsyncImage(
            pictureUrl = avatarUrl,
        )
    }
}