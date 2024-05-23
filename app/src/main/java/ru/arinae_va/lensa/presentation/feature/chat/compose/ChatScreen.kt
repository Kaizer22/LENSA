package ru.arinae_va.lensa.presentation.feature.chat.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.domain.model.chat.Chat
import ru.arinae_va.lensa.domain.model.chat.Message
import ru.arinae_va.lensa.domain.model.user.Presence
import ru.arinae_va.lensa.presentation.common.component.FSpace
import ru.arinae_va.lensa.presentation.common.component.HSpace
import ru.arinae_va.lensa.presentation.common.component.LensaAvatar
import ru.arinae_va.lensa.presentation.common.component.LensaIconButton
import ru.arinae_va.lensa.presentation.common.component.LensaInput
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.feature.chat.viewmodel.ChatScreenState
import ru.arinae_va.lensa.presentation.feature.chat.viewmodel.ChatViewModel
import ru.arinae_va.lensa.presentation.theme.LensaTheme
import ru.arinae_va.lensa.utils.formatPrettyDatetime

@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
) {
    setSystemUiColor()
    val state by viewModel.state.collectAsState()
    ChatScreenContent(
        state = state,
        onMessageInputChanged = viewModel::onMessageInputChanged,
        onBackPressed = viewModel::onBackPressed,
        onEditMessageClick = viewModel::onEditMessageClick,
        onDeleteMessageClick = viewModel::onDeleteMessageClick,
        onSendMessageClick = viewModel::onSendMessageClick,
        onCancelEditing = viewModel::onCancelEditing,
        onAvatarClick = viewModel::onAvatarClick,
    )
}

private val MESSAGE_INPUT_HEIGHT = 64.dp
private val TOP_BAR_HEIGHT = 60.dp

@Composable
fun ChatScreenContent(
    state: ChatScreenState,
    onMessageInputChanged: (String) -> Unit,
    onBackPressed: () -> Unit,
    onEditMessageClick: (String) -> Unit,
    onDeleteMessageClick: (String) -> Unit,
    onCancelEditing: () -> Unit,
    onSendMessageClick: () -> Unit,
    onAvatarClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.background(color = LensaTheme.colors.backgroundColor),
        topBar = {
            ChatTopBar(
                currentProfileId = state.currentProfileId,
                chat = state.chat,
                presence = state.interlocutorPresence,
                onAvatarClick = onAvatarClick,
                onBackPressed = onBackPressed,
            )
        },
        bottomBar = {
            MessageInput(
                isEditing = state.editingMessageId != null,
                input = state.messageInput,
                onValueChanged = onMessageInputChanged,
                onSendMessage = onSendMessageClick,
                onCancelEditing = onCancelEditing,
            )
        },
    ) { paddingValues ->
        val scrollState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()
        LaunchedEffect(key1 = state.messages.size) {
            if (state.messages.isNotEmpty()) {
                coroutineScope.launch {
                    scrollState.scrollToItem(state.messages.size - 1)
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = LensaTheme.colors.backgroundColor),
            contentAlignment = Alignment.BottomCenter,
        ) {
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
            ) {
                itemsIndexed(state.messages) { index, message ->
                    if (isNeedToShowDay(state.messages, index, message)) {
                        DayRow(message.dateTime)
                    }
                    VSpace(h = 4.dp)
                    MessageRow(
                        message = message,
                        isEditing = message.messageId == state.editingMessageId,
                        isReceived = message.authorProfileId != state.currentProfileId,
                        showArrow = isNeedToShowArrow(state.messages, index, message),
                        onEditMessage = { onEditMessageClick.invoke(message.messageId) },
                        onDeleteMessage = { onDeleteMessageClick.invoke(message.messageId) },
                    )
                }
            }
        }
    }
}

fun isNeedToShowDay(messages: List<Message>, index: Int, message: Message): Boolean =
    if (index == 0) true else {
        val previousMessage = messages[index - 1]
        message.dateTime.year != previousMessage.dateTime.year ||
                message.dateTime.dayOfYear != previousMessage.dateTime.dayOfYear
    }

fun isNeedToShowArrow(messages: List<Message>, index: Int, message: Message): Boolean =
    index == 0 || message.authorProfileId != messages[index - 1].authorProfileId

@Composable
fun ChatTopBar(
    currentProfileId: String,
    chat: Chat?,
    presence: Presence?,
    onAvatarClick: () -> Unit,
    onBackPressed: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(color = LensaTheme.colors.backgroundColor)
            .padding(
                horizontal = 16.dp,
            )
    ) {
        Row(
            modifier = Modifier
                .height(TOP_BAR_HEIGHT)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LensaIconButton(
                onClick = onBackPressed,
                icon = R.drawable.ic_arrow_back,
                iconSize = 28.dp,
            )
            FSpace()
            val chatAvatarUrl = remember(chat) {
                chat?.getAvatarUrl(currentProfileId)
            }
            val chatName = remember(chat) {
                chat?.getChatName(currentProfileId)
            }
//            val specialization = remember(chat) {
//                chat?.getSpecailization(currentProfileId)
//            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = chatName.orEmpty().uppercase(),
                    style = LensaTheme.typography.name,
                    color = LensaTheme.colors.textColor,
                )
                presence?.let {
                    Text(
                        text = if (it.isOnline) "В сети" else "Был(-а) в сети ${
                            formatPrettyDatetime(
                                it.lastOnline
                            )
                        }",
                        style = LensaTheme.typography.signature,
                        color = LensaTheme.colors.textColorSecondary,
                    )
                }
//                specialization?.let {
//                    Text(
//                        text = specialization,
//                        style = LensaTheme.typography.signature,
//                        color = LensaTheme.colors.textColorSecondary,
//                    )
//                }
            }
            FSpace()
            LensaAvatar(
                avatarUrl = chatAvatarUrl.orEmpty(),
                onClick = onAvatarClick,
            )
        }
        HSpace(w = 12.dp)
        Divider(color = LensaTheme.colors.textColor)
    }
}

@Composable
fun MessageInput(
    isEditing: Boolean,
    input: String,
    onValueChanged: (String) -> Unit,
    onSendMessage: () -> Unit,
    onCancelEditing: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(color = LensaTheme.colors.backgroundColor)
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp,
            ),
    ) {
        if (isEditing) {
            Row {
                Text(
                    text = "Редактирование",
                    style = LensaTheme.typography.text,
                    color = LensaTheme.colors.textColor,
                )
                FSpace()
                LensaIconButton(
                    onClick = onCancelEditing,
                    icon = R.drawable.ic_cancel,
                    iconSize = 24.dp,
                )
                HSpace(w = 16.dp)
            }
            VSpace(h = 16.dp)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                LensaInput(
                    placeholder = "Введите сообщение...",
                    onValueChanged = onValueChanged,
                    value = input,
                    isRoundedShape = true,
                )
            }
            HSpace(w = 16.dp)
            LensaIconButton(
                onClick = {
                    if (input.isNotEmpty()) onSendMessage.invoke()
                },
                icon = R.drawable.ic_arrow_forward,
                iconSize = 28.dp,
            )
        }
    }
}