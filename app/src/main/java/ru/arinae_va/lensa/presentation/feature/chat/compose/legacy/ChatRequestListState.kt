package ru.arinae_va.lensa.presentation.feature.chat.compose.legacy

import ru.arinae_va.lensa.domain.model.legacy.ChatRequest

data class ChatRequestListState(
    val chatRequests: List<ChatRequest>,
    val isLoading: Boolean,
)