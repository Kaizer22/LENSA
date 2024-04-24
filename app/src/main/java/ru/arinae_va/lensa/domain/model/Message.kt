package ru.arinae_va.lensa.domain.model

import java.time.LocalDateTime

data class Message (
    val messageId: String,
    val authorProfileId: String,
    val chatId: String,
    val message: String,
    val dateTime: LocalDateTime,
)