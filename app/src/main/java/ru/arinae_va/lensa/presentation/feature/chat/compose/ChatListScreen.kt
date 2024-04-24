package ru.arinae_va.lensa.presentation.feature.chat.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.feature.chat.viewmodel.ChatListState
import ru.arinae_va.lensa.presentation.feature.chat.viewmodel.ChatListViewModel

@Composable
fun ChatListScreen(
    viewModel: ChatListViewModel,
) {
    setSystemUiColor()
    val state by viewModel.state.collectAsState()
    ChatListContent(
        state = state
    )
}

@Composable
private fun ChatListContent(
    state: ChatListState,
) {

}