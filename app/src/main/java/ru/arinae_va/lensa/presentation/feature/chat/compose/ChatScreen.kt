package ru.arinae_va.lensa.presentation.feature.chat.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.common.component.FSpace
import ru.arinae_va.lensa.presentation.common.component.LensaIconButton
import ru.arinae_va.lensa.presentation.common.component.LensaInput
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

@Composable
fun ChatScreenContent(
    state: ChatScreenState,
    onMessageInputChanged: (String) -> Unit,
    onBackPressed: () -> Unit,
    onEditMessageClick: (String) -> Unit,
    onDeleteMessageClick: (String) -> Unit,
    onSendMessageClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = LensaTheme.colors.backgroundColor)
    ) {
        Row {
            LensaIconButton(
                onClick = onBackPressed,
                icon = R.drawable.ic_arrow_back,
                iconSize = 28.dp,
            )
        }
        LazyColumn() {//modifier = Modifier.fillMaxSize()) {
            items(state.messages) { message ->
                MessageItem(
                    message = message,
                    onEditMessage = {
                        onEditMessageClick.invoke(message.messageId)
                    },
                    onDeleteMessage = {
                        onDeleteMessageClick.invoke(message.messageId)
                    },
                )
            }
        }
        MessageInput(
            input = state.messageInput,
            onValueChanged = onMessageInputChanged,
            onSendMessage = onSendMessageClick,
        )
    }
}

@Composable
fun MessageInput(
    input: String,
    onValueChanged: (String) -> Unit,
    onSendMessage: () -> Unit,
) {
    Row {
        LensaInput(
            modifier = Modifier.width(290.dp),
            onValueChanged = onValueChanged,
            value = input,
            isRoundedShape = true,
        )
        FSpace()
        LensaIconButton(
            onClick = onSendMessage,
            icon = R.drawable.ic_arrow_forward,
            iconSize = 28.dp,
        )
    }
}