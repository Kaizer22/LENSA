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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.domain.model.ChatRequest
import ru.arinae_va.lensa.presentation.common.component.FSpace
import ru.arinae_va.lensa.presentation.common.component.LensaIconButton
import ru.arinae_va.lensa.presentation.common.component.VSpace
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.feature.chat.viewmodel.ChatRequestListState
import ru.arinae_va.lensa.presentation.feature.chat.viewmodel.ChatRequestListViewModel
import ru.arinae_va.lensa.presentation.theme.LensaTheme

@Composable
fun ChatRequestListScreen(
    viewModel: ChatRequestListViewModel,
) {
    setSystemUiColor()
    val state by viewModel.state.collectAsState()
    ChatRequestListContent(
        state = state,
        onBackPressed = viewModel::onBackPressed,
        onAcceptRequest = viewModel::onAcceptRequest,
        onCancelRequest = viewModel::onCancelRequest,
    )
}

@Composable
private fun ChatRequestListContent(
    state: ChatRequestListState,
    onAcceptRequest: (ChatRequest) -> Unit,
    onCancelRequest: (ChatRequest) -> Unit,
    onBackPressed: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = LensaTheme.colors.backgroundColor)
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 24.dp,
            )
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            LensaIconButton(
                onClick = onBackPressed,
                icon = R.drawable.ic_arrow_back,
                iconSize = 28.dp,
            )
            FSpace()
        }
        VSpace(h = 24.dp)
        Text(
            text = "ЗАПРОСЫ",
            style = LensaTheme.typography.header2,
            color = LensaTheme.colors.textColor,
        )
        VSpace(h = 12.dp)
        Divider(color = LensaTheme.colors.dividerColor)
        VSpace(h = 24.dp)

        if (state.chatRequests.isNotEmpty()) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(items = state.chatRequests) { chatRequest ->
                    ChatRequestItem(
                        authorName = chatRequest.authorName,
                        avatarUrl = chatRequest.authorAvatarUrl.orEmpty(),
                        onAcceptRequest = {
                            onAcceptRequest.invoke(chatRequest)
                        },
                        onCancelRequest = {
                            onCancelRequest.invoke(chatRequest)
                        },
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                FSpace()
                Text(
                    text = "Нет входящих запросов",
                    style = LensaTheme.typography.header3,
                    color = LensaTheme.colors.textColor,
                )
                FSpace()
            }
        }
    }
}

@Composable
@Preview
fun ChatRequestListPreview() {
    LensaTheme {
        ChatRequestListContent(
            state = ChatRequestListState(
                chatRequests = emptyList(),
                isLoading = false,
            ),
            onAcceptRequest = {},
            onCancelRequest = {},
            onBackPressed = {}
        )
    }
}