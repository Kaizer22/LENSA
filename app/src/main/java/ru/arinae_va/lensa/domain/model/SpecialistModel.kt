package ru.arinae_va.lensa.domain.model

import ru.arinae_va.lensa.presentation.feature.feed.compose.SocialMediaType
import java.time.LocalDateTime
import java.util.UUID

data class SpecialistModel(
    val id: String,
    val name: String,
    val surname: String,
    val specialization: String,
    val rating: Float,
    val avatarUrl: String,
    val country: String,
    val city: String,
    val personalSite: String,
    val email: String,
    val socialMedias: List<SocialMedia>,
    val about: String,
    val portfolioUrls: List<String>,
    val prices: List<Price>,
    val reviews: List<Review>,
)

data class Review(
    val name: String,
    val surname: String,
    val avatarUrl: String,
    val dateTime: LocalDateTime,
)

data class Price(
    val id: String = UUID.randomUUID().toString(),
    var name: String,
    var text: String,
    var price: Int,
    val currency: PriceCurrency,
)

enum class PriceCurrency(
    val symbol: String,
) {
    RUB(symbol = "₽")
}

data class SocialMedia(
    val link: String,
    val type: SocialMediaType,
)