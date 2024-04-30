package ru.arinae_va.lensa.presentation.feature.chat.compose

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.presentation.common.component.FSpace
import ru.arinae_va.lensa.presentation.common.component.HSpace
import ru.arinae_va.lensa.presentation.common.component.LensaAvatar
import ru.arinae_va.lensa.presentation.common.component.LensaButton
import ru.arinae_va.lensa.presentation.common.component.LensaTextButton
import ru.arinae_va.lensa.presentation.common.component.LensaTextButtonType
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun ChatRequestItem(
    authorName: String,
    avatarUrl: String,
    authorSpecialization: String,
    onAcceptRequest: () -> Unit,
    onCancelRequest: () -> Unit,
    onAvatarClick: () -> Unit,
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
            LensaAvatar(avatarUrl = avatarUrl, onClick = onAvatarClick)
            HSpace(w = 24.dp)
            Column {
                Text(
                    text = authorName,
                    style = LensaTheme.typography.text,
                    color = LensaTheme.colors.textColor,
                )
                Text(
                    text = authorSpecialization,
                    style = LensaTheme.typography.signature,
                    color = LensaTheme.colors.textColor,
                )
            }
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
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LensaButton(
                text = "ПРИНЯТЬ",
                onClick = onAcceptRequest,
            )
            FSpace()
            LensaTextButton(
                text = "ОТКЛОНИТЬ",
                type = LensaTextButtonType.DEFAULT,
                onClick = onCancelRequest,
            )
        }
    }
}