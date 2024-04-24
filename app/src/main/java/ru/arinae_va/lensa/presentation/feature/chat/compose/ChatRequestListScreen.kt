package ru.arinae_va.lensa.presentation.feature.chat.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ru.arinae_va.lensa.presentation.common.utils.setSystemUiColor
import ru.arinae_va.lensa.presentation.feature.chat.viewmodel.ChatRequestListState
import ru.arinae_va.lensa.presentation.feature.chat.viewmodel.ChatRequestListViewModel

@Composable
fun ChatRequestListScreen(
    viewModel: ChatRequestListViewModel,
) {
    setSystemUiColor()
    val state by viewModel.state.collectAsState()
    ChatRequestListContent(
        state = state,
    )
}

@Composable
private fun ChatRequestListContent(
    state: ChatRequestListState,
) {

}