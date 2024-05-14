package ru.arinae_va.lensa.presentation.feature.chat.viewmodel

import ru.arinae_va.lensa.domain.model.chats.Chat
import ru.arinae_va.lensa.domain.model.chats.Message

data class ChatListState(
    val currentUserId: String,
    val chats: List<Chat>,
    val latestMessages: List<Message>,
    val isLoading: Boolean,
)