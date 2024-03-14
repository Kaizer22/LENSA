package ru.arinae_va.lensa.data.model

import ru.arinae_va.lensa.presentation.feature.feed.compose.SocialMediaType

data class SocialMediaResponse(
    val link: String? = null,
    val type: SocialMediaType? = null,
)