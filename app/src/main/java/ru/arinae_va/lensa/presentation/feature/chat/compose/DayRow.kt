package ru.arinae_va.lensa.presentation.feature.chat.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.presentation.theme.LensaTheme
import ru.arinae_va.lensa.utils.formatChatDate
import java.time.LocalDateTime

@Composable
fun DayRow(dateTime: LocalDateTime) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(14.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier.background(
                color = LensaTheme.colors.receivedMessage.copy(alpha = 0.7f),
                shape = LensaTheme.shapes.roundShape,
            ).padding(8.dp)
        ) {
            Text(
                text = formatChatDate(dateTime),
                style = LensaTheme.typography.smallAccent,
                color = LensaTheme.colors.textColor,
            )
        }
    }
}