package ru.arinae_va.lensa.presentation.feature.chat.viewmodel

import ru.arinae_va.lensa.domain.model.chat.Chat
import ru.arinae_va.lensa.domain.model.chat.Message
import ru.arinae_va.lensa.domain.model.user.Presence

data class ChatScreenState(
    val currentProfileId: String,
    val isLoading: Boolean,
    val chat: Chat?,
    val pinnedMessage: Message?,
    val messages: List<Message>,
    val messageInput: String,
    val interlocutorPresence: Presence? = null,
    val editingMessageId: String? = null,
)