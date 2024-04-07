package ru.arinae_va.lensa.domain.model

import java.time.LocalDateTime

data class Review(
    val authorId: String,
    val profileId: String,
    val name: String,
    val surname: String,
    val rating: Float,
    val text: String,
    val avatarUrl: String,
    val dateTime: LocalDateTime,
)