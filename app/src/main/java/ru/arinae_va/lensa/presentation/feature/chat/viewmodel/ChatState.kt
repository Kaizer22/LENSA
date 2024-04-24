package ru.arinae_va.lensa.presentation.feature.chat.viewmodel

import ru.arinae_va.lensa.domain.model.Message

data class ChatState(
    val messages: List<Message>,
    val messageInput: String,
)