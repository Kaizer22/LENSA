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
import ru.arinae_va.lensa.domain.model.Chat
import ru.arinae_va.lensa.domain.model.Message
import ru.arinae_va.lensa.presentation.common.component.HSpace
import ru.arinae_va.lensa.presentation.common.component.LensaAvatar
import ru.arinae_va.lensa.presentation.common.component.LensaIconButton
import ru.arinae_va.lensa.presentation.common.component.LensaInput
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.feature.chat.viewmodel.ChatScreenState
import ru.arinae_va.lensa.presentation.feature.chat.viewmodel.ChatViewModel
import ru.arinae_va.lensa.presentation.theme.LensaTheme

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
    onSendMessageClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.background(color = LensaTheme.colors.backgroundColor),
        topBar = {
            ChatTopBar(
                currentProfileId = state.currentProfileId,
                chat = state.chat,
                onBackPressed = onBackPressed,
            )
        },
        bottomBar = {
            MessageInput(
                modifier = Modifier.height(MESSAGE_INPUT_HEIGHT),
                input = state.messageInput,
                onValueChanged = onMessageInputChanged,
                onSendMessage = onSendMessageClick,
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
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter,
        ) {
            LazyColumn(
                state = scrollState,
                modifier = Modifier.padding(paddingValues),
            ) {
                itemsIndexed(state.messages) { index, message ->
                    if (isNeedToShowDay(state.messages, index, message)) {
                        DayRow(message.dateTime)
                    }
                    MessageRow(
                        message = message,
                        isReceived = message.authorProfileId != state.currentProfileId,
                        showArrow = isNeedToShowArrow(state.messages, index, message),
                        onEditMessage = {},
                        onDeleteMessage = {},
                    )
                    VSpace(h = 8.dp)
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
    onBackPressed: () -> Unit,
) {
    Row(
        modifier = Modifier
            .height(TOP_BAR_HEIGHT)
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp,
            )
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LensaIconButton(
            onClick = onBackPressed,
            icon = R.drawable.ic_arrow_back,
            iconSize = 28.dp,
        )
        HSpace(w = 24.dp)
        val chatAvatarUrl = remember(chat) {
            chat?.getAvatarUrl(currentProfileId)
        }
        val chatName = remember(chat) {
            chat?.getChatName(currentProfileId)
        }
        val specialization = remember(chat) {
            chat?.getSpecailization(currentProfileId)
        }
        LensaAvatar(avatarUrl = chatAvatarUrl.orEmpty())
        HSpace(w = 12.dp)
        Column {
            Text(
                text = chatName.orEmpty(),
                style = LensaTheme.typography.name,
                color = LensaTheme.colors.textColor,
            )
            specialization?.let {
                Text(
                    text = specialization,
                    style = LensaTheme.typography.signature,
                    color = LensaTheme.colors.textColor,
                )
            }
        }

    }
}

@Composable
fun MessageInput(
    modifier: Modifier = Modifier,
    input: String,
    onValueChanged: (String) -> Unit,
    onSendMessage: () -> Unit,
) {
    Row(
        modifier = Modifier.padding(
            horizontal = 16.dp,
            vertical = 12.dp,
        ),
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