package ru.arinae_va.lensa.domain.model.user

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