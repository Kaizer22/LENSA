package ru.arinae_va.lensa.presentation.feature.chat.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smarttoolfactory.bubble.ArrowAlignment
import com.smarttoolfactory.bubble.ArrowShape
import com.smarttoolfactory.bubble.bubble
import com.smarttoolfactory.bubble.rememberBubbleState
import ru.arinae_va.lensa.domain.model.Message
import ru.arinae_va.lensa.presentation.common.component.FSpace
import ru.arinae_va.lensa.presentation.theme.LensaTheme
import ru.arinae_va.lensa.utils.formatMessageDatetime
import java.time.LocalDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageRow(
    message: Message,
    isEditing: Boolean = false,
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
    var isActionMenuVisible by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (isEditing) LensaTheme.colors.textColor
                else Color.Transparent
            ),
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
                .combinedClickable(
                    onClick = {},
                    onLongClick = { isActionMenuVisible = true }
                )
                .padding(8.dp)
        ) {
            if (!isReceived) {
                DropdownMenu(
                    modifier = Modifier.background(color = LensaTheme.colors.fadeColor),
                    expanded = isActionMenuVisible,
                    onDismissRequest = { isActionMenuVisible = false }) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "РЕДАКТИРОВАТЬ",
                                style = LensaTheme.typography.text,
                                color = LensaTheme.colors.textColor,
                            )
                        },
                        onClick = {
                            onEditMessage.invoke()
                            isActionMenuVisible = false
                        },
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "УДАЛИТЬ",
                                style = LensaTheme.typography.text,
                                color = LensaTheme.colors.textColor,
                            )
                        },
                        onClick = {
                            onDeleteMessage.invoke()
                            isActionMenuVisible = false
                        }
                    )
                }
            }
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

@Preview
@Composable
fun MessagePreview() {
    LensaTheme {
        Column {
            MessageRow(
                message = Message(
                    messageId = "",
                    authorProfileId = "",
                    chatId = "",
                    message = "Test message",
                    dateTime = LocalDateTime.now(),
                ),
                isReceived = true,
                showArrow = true,
                onEditMessage = {},
                onDeleteMessage = {}
            )
            MessageRow(
                message = Message(
                    messageId = "",
                    authorProfileId = "",
                    chatId = "",
                    message = "Test message",
                    dateTime = LocalDateTime.now(),
                ),
                isReceived = false,
                showArrow = true,
                onEditMessage = {},
                onDeleteMessage = {}
            )
            MessageRow(
                message = Message(
                    messageId = "",
                    authorProfileId = "",
                    chatId = "",
                    message = "Test message",
                    dateTime = LocalDateTime.now(),
                ),
                isEditing = true,
                isReceived = false,
                showArrow = false,
                onEditMessage = {},
                onDeleteMessage = {}
            )
        }
    }
}