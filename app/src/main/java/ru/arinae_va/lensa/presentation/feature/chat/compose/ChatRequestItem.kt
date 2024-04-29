package ru.arinae_va.lensa.presentation.feature.chat.compose

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.presentation.common.component.HSpace
import ru.arinae_va.lensa.presentation.common.component.LensaAsyncImage
import ru.arinae_va.lensa.presentation.common.component.LensaButton
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun ChatRequestItem(
    authorName: String,
    avatarUrl: String,
    onAcceptRequest: () -> Unit,
    onCancelRequest: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(4.dp)
            .border(
                width = 1.dp,
                color = LensaTheme.colors.textColor,
            )
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(shape = LensaTheme.shapes.roundShape),
            ) {
                LensaAsyncImage(
                    pictureUrl = avatarUrl,
                )
            }
            HSpace(w = 24.dp)
            Text(
                text = authorName,
                style = LensaTheme.typography.text,
                color = LensaTheme.colors.textColor,
            )
        }
        VSpace(h = 16.dp)
        Text(
            text = "Хочет начать с вами диалог",
            style = LensaTheme.typography.text,
            color = LensaTheme.colors.textColor,
        )
        VSpace(h = 16.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            LensaButton(
                text = "Принять",
                onClick = onAcceptRequest,
            )
            LensaButton(
                text = "Отклонить",
                onClick = onCancelRequest,
            )
        }
    }
}