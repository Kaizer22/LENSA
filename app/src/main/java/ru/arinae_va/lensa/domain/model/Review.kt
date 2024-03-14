package ru.arinae_va.lensa.domain.model

import java.time.LocalDateTime

data class Review(
    val name: String,
    val surname: String,
    val avatarUrl: String,
    val dateTime: LocalDateTime,
)