package ru.arinae_va.lensa.presentation.feature.chat.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smarttoolfactory.bubble.ArrowAlignment
import com.smarttoolfactory.bubble.ArrowShape
import com.smarttoolfactory.bubble.bubble
import com.smarttoolfactory.bubble.rememberBubbleState
import ru.arinae_va.lensa.domain.model.Message
import ru.arinae_va.lensa.presentation.common.component.FSpace
import ru.arinae_va.lensa.presentation.theme.LensaTheme
import ru.arinae_va.lensa.utils.formatMessageDatetime

@Composable
fun MessageRow(
    message: Message,
    isReceived: Boolean = true,
    showArrow: Boolean,
    onEditMessage: () -> Unit,
    onDeleteMessage: () -> Unit,
) {
    val bubbleState = rememberBubbleState(
        cornerRadius = 8.dp,
        alignment = if (isReceived) ArrowAlignment.LeftTop
        else ArrowAlignment.RightTop,
        arrowShape = ArrowShape.HalfTriangle,
        arrowOffsetX = 0.dp,
        arrowOffsetY = 0.dp,
        arrowWidth = 14.dp,
        arrowHeight = 14.dp,
        drawArrow = showArrow,
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isReceived) Arrangement.Start
        else Arrangement.End,
    ) {
        if (!isReceived) FSpace()
        Column(
            modifier = Modifier
                .weight(2.5f)
                .bubble(
                    bubbleState = bubbleState,
                    color = if (isReceived) LensaTheme.colors.receivedMessage
                    else LensaTheme.colors.sentMessageColor,
                )
                .padding(8.dp)
        ) {
            Text(
                text = message.message,
                style = LensaTheme.typography.text,
                color = LensaTheme.colors.textColor,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                Text(
                    text = formatMessageDatetime(message.dateTime),
                    style = LensaTheme.typography.signature,
                    color = LensaTheme.colors.textColor,
                )
            }
        }
        if (isReceived) FSpace()
    }
}