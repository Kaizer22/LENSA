package ru.arinae_va.lensa.presentation.feature.chat.viewmodel

import ru.arinae_va.lensa.domain.model.Chat

data class ChatListState(
    val chats: List<Chat>,
    val isLoading: Boolean,
)