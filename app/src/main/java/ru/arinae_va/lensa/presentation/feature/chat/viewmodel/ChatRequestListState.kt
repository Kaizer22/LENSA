package ru.arinae_va.lensa.presentation.feature.chat.viewmodel

import ru.arinae_va.lensa.domain.model.ChatRequest

data class ChatRequestListState(
    val chatRequests: List<ChatRequest>,
    val isLoading: Boolean,
)