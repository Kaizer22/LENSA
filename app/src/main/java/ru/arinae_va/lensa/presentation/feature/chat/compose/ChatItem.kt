package ru.arinae_va.lensa.presentation.feature.chat.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.domain.model.Chat
import ru.arinae_va.lensa.domain.model.Message
import ru.arinae_va.lensa.presentation.common.component.HSpace
import ru.arinae_va.lensa.presentation.common.component.LensaAvatar
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.theme.LensaTheme
import ru.arinae_va.lensa.utils.formatPrettyDatetime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatItem(
    currentUserId: String,
    chat: Chat,
    latestMessage: Message?,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    if (currentUserId.isNotEmpty()) {
        val avatarUrl = remember { chat.getAvatarUrl(currentUserId) }
        val chatName = remember { chat.getChatName(currentUserId) }
        val specialization = remember { chat.getSpecailization(currentUserId) }
        var isActionMenuVisible by remember { mutableStateOf(false) }
        Column {
            DropdownMenu(
                expanded = isActionMenuVisible,
                onDismissRequest = { isActionMenuVisible = false }) {
                DropdownMenuItem(
                    text = { Text("Редактировать") },
                    onClick = onEditClick
                )
                DropdownMenuItem(
                    text = { Text("Удалить") },
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
                        horizontal = 8.dp,
                        vertical = 8.dp,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LensaAvatar(avatarUrl = avatarUrl)
                HSpace(w = 24.dp)
                Column {
                    Text(
                        text = chatName,
                        style = LensaTheme.typography.name,
                        color = LensaTheme.colors.textColor,
                    )
                    specialization?.let {
                        Text(
                            text = specialization,
                            style = LensaTheme.typography.smallAccent,
                            color = LensaTheme.colors.textColor,
                        )
                    }
                    VSpace(h = 8.dp)
                    Text(
                        text = latestMessage?.message ?: "Диалог создан",
                        style = LensaTheme.typography.signature,
                        color = LensaTheme.colors.textColor,
                    )
                }
                latestMessage?.let {
                    Text(
                        text = formatPrettyDatetime(latestMessage.dateTime)
                    )
                }
            }
            Divider(color = LensaTheme.colors.dividerColor)
        }
    }
}