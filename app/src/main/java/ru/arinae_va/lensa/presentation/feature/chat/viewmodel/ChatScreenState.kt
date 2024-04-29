package ru.arinae_va.lensa.presentation.feature.chat.viewmodel

import ru.arinae_va.lensa.domain.model.Chat
import ru.arinae_va.lensa.domain.model.Message

data class ChatScreenState(
    val currentProfileId: String,
    val isLoading: Boolean,
    val chat: Chat?,
    val messages: List<Message>,
    val messageInput: String,
)