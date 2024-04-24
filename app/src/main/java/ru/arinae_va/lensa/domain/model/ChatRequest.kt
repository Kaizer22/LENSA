package ru.arinae_va.lensa.domain.model

import java.time.LocalDateTime

data class ChatRequest(
    // authorID_targetID
    val requestId: String,
    val authorProfileId: String,
    val targetProfileId: String,
    val dateTime: LocalDateTime,
)
