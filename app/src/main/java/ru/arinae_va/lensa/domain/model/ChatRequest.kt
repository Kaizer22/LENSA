package ru.arinae_va.lensa.domain.model

import java.time.LocalDateTime

data class ChatRequest(
    // authorID_targetID
    val requestId: String,
    val authorAvatarUrl: String?,
    val targetAvatarUrl: String?,
    val targetName: String,
    val authorName: String,
    val targetSpecialization: String,
    val authorSpecialization: String,
    val authorProfileId: String,
    val targetProfileId: String,
    val dateTime: LocalDateTime,
)
