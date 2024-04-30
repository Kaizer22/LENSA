package ru.arinae_va.lensa.presentation.feature.chat.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.common.component.FSpace
import ru.arinae_va.lensa.presentation.common.component.HSpace
import ru.arinae_va.lensa.presentation.common.component.LensaAlertDialog
import ru.arinae_va.lensa.presentation.common.component.LensaIconButton
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.feature.chat.viewmodel.ChatListState
import ru.arinae_va.lensa.presentation.feature.chat.viewmodel.ChatListViewModel
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun ChatListScreen(
    viewModel: ChatListViewModel,
) {
    setSystemUiColor()
    val state by viewModel.state.collectAsState()
    ChatListContent(
        state = state,
        onAddGroupClick = viewModel::onAddGroupClick,
        onChatRequestListClick = viewModel::onChatRequestListClick,
        onChatClick = viewModel::onChatClick,
        onEditChatClick = viewModel::onEditChatClick,
        onDeleteChatClick = viewModel::onDeleteChatClick,
        onBackPressed = viewModel::onBackPressed,
    )
}

@Composable
private fun ChatListContent(
    state: ChatListState,
    onChatRequestListClick: () -> Unit,
    onAddGroupClick: () -> Unit,
    onChatClick: (String) -> Unit,
    onEditChatClick: () -> Unit,
    onDeleteChatClick: (String) -> Unit,
    onBackPressed: () -> Unit,
) {

    var isShowDeleteChatDialog by remember { mutableStateOf(false) }
    var chatToDeleteId by remember { mutableStateOf("") }
    if (isShowDeleteChatDialog) {
        LensaAlertDialog(
            onConfirmClick = { onDeleteChatClick.invoke(chatToDeleteId) },
            onDismissClick = { isShowDeleteChatDialog = false },
            title = "УДАЛЕНИЕ ЧАТА",
            subtitle = "ЭТО БЕЗВОЗВРАТНО УДАЛИТ ЧАТ И ВСЕ СООБЩЕНИЯ В НЕМ. " +
                    "ДЛЯ ВАС И ДЛЯ СОБЕСЕДНИКОВ. " +
                    "ВЫ УВЕРЕНЫ?",
            confirmText = "УДАЛИТЬ",
            dismissText = "ОТМЕНИТЬ"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = LensaTheme.colors.backgroundColor)
            .padding(horizontal = 16.dp)
    ) {
        LazyColumn {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                ) {
                    LensaIconButton(
                        onClick = onBackPressed,
                        icon = R.drawable.ic_arrow_back,
                        iconSize = 28.dp,
                    )
                    FSpace()
                    LensaIconButton(
                        onClick = onChatRequestListClick,
                        icon = R.drawable.ic_chat,
                        iconSize = 28.dp,
                    )
                    HSpace(w = 16.dp)
                    LensaIconButton(
                        onClick = onAddGroupClick,
                        icon = R.drawable.ic_plus,
                        iconSize = 28.dp,
                    )
                }
                VSpace(h = 24.dp)
                Text(
                    text = "ЧАТЫ",
                    style = LensaTheme.typography.header2,
                    color = LensaTheme.colors.textColor,
                )
                VSpace(h = 12.dp)
                Divider(color = LensaTheme.colors.dividerColor)
                VSpace(h = 24.dp)
            }
            if (state.chats.isNotEmpty()) {
                items(state.chats) { chat ->
                    ChatItem(
                        currentUserId = state.currentUserId,
                        chat = chat,
                        latestMessage = state.latestMessages.find {
                            it.chatId == chat.chatId
                        },
                        onClick = {
                            onChatClick.invoke(chat.chatId)
                        },
                        onEditClick = onEditChatClick,
                        onDeleteClick = {
                            chatToDeleteId = chat.chatId
                            isShowDeleteChatDialog = true
                            //onDeleteChatClick.invoke(chat.chatId)
                        }
                    )
                }
            } else {
                item {
                    Text(text = "Тут пока ничего нет")
                }
            }
        }
    }
}