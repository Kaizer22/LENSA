package ru.arinae_va.lensa.presentation.feature.chat.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.arinae_va.lensa.domain.model.chats.Chat
import ru.arinae_va.lensa.domain.model.chats.Message
import ru.arinae_va.lensa.presentation.common.component.FSpace
import ru.arinae_va.lensa.presentation.common.component.HSpace
import ru.arinae_va.lensa.presentation.common.component.LensaAvatar
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.theme.LensaTheme
import ru.arinae_va.lensa.utils.ext.toDp
import ru.arinae_va.lensa.utils.formatPrettyDatetime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatItem(
    currentUserId: String,
    chat: Chat,
    latestMessage: Message?,
    onClick: () -> Unit,
    onBlockClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    if (currentUserId.isNotEmpty()) {
        val avatarUrl = remember { chat.getAvatarUrl(currentUserId) }
        val chatName = remember { chat.getChatName(currentUserId) }
        val specialization = remember { chat.getSpecailization(currentUserId) }
        var isActionMenuVisible by remember { mutableStateOf(false) }
        Column {
            DropdownMenu(
                modifier = Modifier.background(color = LensaTheme.colors.fadeColor),
                expanded = isActionMenuVisible,
                onDismissRequest = { isActionMenuVisible = false }) {
                // if (!chat.isDialog()) { }
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "ЗАБЛОКИРОВАТЬ",
                            style = LensaTheme.typography.text,
                            color = LensaTheme.colors.textColor,
                        )
                    },
                    onClick = onBlockClick
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "УДАЛИТЬ",
                            style = LensaTheme.typography.text,
                            color = LensaTheme.colors.textColor,
                        )
                    },
                    onClick = onDeleteClick
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .combinedClickable(
                        onClick = onClick,
                        onLongClick = { isActionMenuVisible = true },
                    )
                    .padding(
                        vertical = 12.dp,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                HSpace(w = 4.dp)
                LensaAvatar(avatarUrl = avatarUrl)
                HSpace(w = 12.dp)
                Column {
                    Row {
                        Column {
                            Text(
                                text = chatName.uppercase(),
                                style = LensaTheme.typography.name,
                                color = LensaTheme.colors.textColor,
                            )
//                            specialization?.let {
//                                Text(
//                                    text = specialization,
//                                    style = LensaTheme.typography.smallAccent,
//                                    color = LensaTheme.colors.textColor,
//                                )
//                            }
                        }
                        FSpace()
                        latestMessage?.let {
                            Text(
                                text = formatPrettyDatetime(latestMessage.dateTime),
                                style = LensaTheme.typography.signature,
                                color = LensaTheme.colors.textColorSecondary,
                            )
                        }
                    }
                    VSpace(h = 8.dp)
                    val context = LocalContext.current
                    Row(Modifier.heightIn(max = 20.sp.toDp(context))) {
                        val messagePrefix = if (currentUserId == latestMessage?.authorProfileId)
                            "Вы" else chatName.split(" ")[0]
                        val latestMessageText = if (latestMessage?.message == null) {
                            "-Диалог создан-"
                        } else "$messagePrefix: " + latestMessage.message
                        Text(
                            text = latestMessageText,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = LensaTheme.typography.signature,
                            color = LensaTheme.colors.textColor,
                        )
                        FSpace()
                        HSpace(w = 8.dp)

                    }
                }
            }
            Divider(color = LensaTheme.colors.dividerColor)
        }
    }
}