package ru.arinae_va.lensa.domain.model.chats

import java.time.LocalDateTime

data class Message (
    val messageId: String,
    val authorProfileId: String,
    val chatId: String,
    val message: String,
    val isRead: Boolean,
    val dateTime: LocalDateTime,
)