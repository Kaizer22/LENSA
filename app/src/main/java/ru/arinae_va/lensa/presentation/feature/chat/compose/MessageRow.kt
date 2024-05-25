package ru.arinae_va.lensa.presentation.feature.chat.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.domain.model.chat.Message
import ru.arinae_va.lensa.presentation.common.component.HSpace
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
    onPinMessage: () -> Unit,
    onDeleteMessage: () -> Unit,
) {
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
        Column(
            modifier = Modifier
                .background(
                    color = LensaTheme.colors.receivedMessage, //if (isReceived)
                    //else LensaTheme.colors.sentMessageColor,
                    shape = if (showArrow) {
                        if (isReceived) {
                            LensaTheme.shapes.receivedMessageShape
                        } else {
                            LensaTheme.shapes.sentMessageShape
                        }
                    } else LensaTheme.shapes.messageShape
                )
                .widthIn(max = 290.dp)
                .combinedClickable(
                    onClick = {},
                    onLongClick = { isActionMenuVisible = true }
                )
                .padding(
                    horizontal = 18.dp,
                    vertical = 8.dp
                )
        ) {
            if (!isReceived) {
                DropdownMenu(
                    modifier = Modifier.background(color = LensaTheme.colors.fadeColor),
                    expanded = isActionMenuVisible,
                    onDismissRequest = { isActionMenuVisible = false }) {
                    DropdownMenuItem(
                        text = {
                            MessageActionItem(text = "Изменить", icon = R.drawable.ic_pen)
                        },
                        onClick = {
                            onEditMessage.invoke()
                            isActionMenuVisible = false
                        },
                    )
                    DropdownMenuItem(
                        text = {
                            MessageActionItem(
                                text = "Удалить",
                                icon = R.drawable.ic_trash,
                            )
                        },
                        onClick = {
                            onDeleteMessage.invoke()
                            isActionMenuVisible = false
                        },
                    )
                    DropdownMenuItem(
                        text = {
                            MessageActionItem(text = "Закрепить", icon = R.drawable.ic_pin)
                        },
                        onClick = {
                            onPinMessage.invoke()
                            isActionMenuVisible = false
                        },
                    )
                }
            }
            Row(
                //modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(
                    modifier = Modifier.widthIn(max = 200.dp),
                    text = message.message,
                    style = LensaTheme.typography.accentTextButton,
                    color = LensaTheme.colors.textColor,
                )
                HSpace(w = 10.dp)
                Column(
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    Row {
                        Text(
                            text = formatMessageDatetime(message.dateTime),
                            style = LensaTheme.typography.smallAccent,
                            color = LensaTheme.colors.textColorSecondary,
                        )
                        if (!isReceived) {
                            HSpace(w = 4.dp)
                            Icon(
                                painter = painterResource(
                                    id = if (message.isRead)
                                        R.drawable.ic_message_read else R.drawable.ic_message_sent
                                ),
                                tint = LensaTheme.colors.textColorAccent,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MessageActionItem(
    text: String,
    @DrawableRes icon: Int,
) {
    Row(
        modifier = Modifier.padding(
            horizontal = 8.dp,
            vertical = 12.dp,
        )
    ) {
        Text(
            modifier = Modifier.width(180.dp),
            text = text,
            style = LensaTheme.typography.text,
            color = LensaTheme.colors.textColor,
        )
        HSpace(w = 10.dp)
        Icon(
            modifier = Modifier.size(22.dp),
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = LensaTheme.colors.textColor,
        )
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
                    isRead = false,
                    dateTime = LocalDateTime.now(),
                ),
                isReceived = true,
                showArrow = true,
                onEditMessage = {},
                onPinMessage = {},
                onDeleteMessage = {},
            )
            MessageRow(
                message = Message(
                    messageId = "",
                    authorProfileId = "",
                    chatId = "",
                    message = "Test message lonspdkjfpsdjfasp;djkfaskndgdjanfvof jeoinvaiweraoiwnec as o awefjawpeijfp]wijdpnjn asojojdnfosndfpndfno",
                    isRead = false,
                    dateTime = LocalDateTime.now(),
                ),
                isReceived = false,
                showArrow = false,
                onEditMessage = {},
                onPinMessage = {},
                onDeleteMessage = {}
            )
            MessageRow(
                message = Message(
                    messageId = "",
                    authorProfileId = "",
                    chatId = "",
                    message = "Test message",
                    isRead = false,
                    dateTime = LocalDateTime.now(),
                ),
                isEditing = true,
                isReceived = false,
                showArrow = true,
                onEditMessage = {},
                onPinMessage = {},
                onDeleteMessage = {}
            )
        }
    }
}