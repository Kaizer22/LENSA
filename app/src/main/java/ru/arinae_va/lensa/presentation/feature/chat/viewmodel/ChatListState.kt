package ru.arinae_va.lensa.presentation.feature.chat.viewmodel

import ru.arinae_va.lensa.domain.model.chat.Chat
import ru.arinae_va.lensa.domain.model.chat.Message

data class ChatListState(
    val currentUserId: String,
    val chats: List<Chat>,
    val latestMessages: List<Message>,
    val isLoading: Boolean,
)