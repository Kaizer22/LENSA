package ru.arinae_va.lensa.domain.model

import ru.arinae_va.lensa.presentation.feature.feed.compose.SocialMediaType

data class SocialMedia(
    val link: String,
    val type: SocialMediaType,
)