package ru.arinae_va.lensa.domain.model.user

import java.time.LocalDateTime

data class Presence(
    val profileId: String,
    val isOnline: Boolean,
    val lastOnline: LocalDateTime
)