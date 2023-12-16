package ru.arinae_va.lensa.domain.model

import ru.arinae_va.lensa.presentation.feature.feed.compose.SocialMediaType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

data class SpecialistModel(
    val id: String,
    val name: String,
    val surname: String,
    val specialization: String,
    val rating: Float? = null,
    val avatarUrl: String? = null,
    val country: String,
    val city: String,
    val personalSite: String,
    val email: String,
    val socialMedias: List<SocialMedia>,
    val about: String,
    val portfolioUrls: List<String>? = null,
    val prices: List<Price>,
    val reviews: List<Review>? = null,
) {
    companion object {
        val EMPTY = SpecialistModel(
            id = "",
            name = "",
            surname = "",
            specialization = "",
            country = "",
            city = "",
            personalSite = "",
            email = "",
            socialMedias = listOf(),
            about = "",
            prices = listOf(),
        )
    }
}

class SpecialistResponseModel(
    val id: String? = null,
    val name: String? = null,
    val surname: String? = null,
    val specialization: String? = null,
    val rating: Float? = null,
    val avatarUrl: String? = null,
    val country: String? = null,
    val city: String? = null,
    val personalSite: String? = null,
    val email: String? = null,
    val socialMedias: List<SocialMediaResponseModel>? = null,
    val about: String? = null,
    val portfolioUrls: List<String>? = null,
    val prices: List<PriceResponseModel>? = null,
    val reviews: List<ReviewResponseModel>? = null,
) {
    fun mapToSpecialistModel(): SpecialistModel = SpecialistModel(
        id = id.orEmpty(),
        name = name.orEmpty(),
        surname = surname.orEmpty(),
        specialization = specialization.orEmpty(),
        rating = rating,
        avatarUrl = avatarUrl,
        country = country.orEmpty(),
        city = city.orEmpty(),
        personalSite = personalSite.orEmpty(),
        email = email.orEmpty(),
        socialMedias = socialMedias.orEmpty().map {
            mapToSocialMedia(it)
        },
        about = about.orEmpty(),
        portfolioUrls = portfolioUrls,
        prices = prices.orEmpty().map {
            mapToPrice(it)
        },
        reviews = reviews.orEmpty().map {
            mapToReview(it)
        },
    )

    private fun mapToReview(reviewResponseModel: ReviewResponseModel): Review {
        val parsedDt = LocalDateTime.parse(
            reviewResponseModel.dateTime,
            DateTimeFormatter.ISO_DATE_TIME
        )
        return Review(
            name = reviewResponseModel.name.orEmpty(),
            surname = reviewResponseModel.surname.orEmpty(),
            avatarUrl = reviewResponseModel.avatarUrl.orEmpty(),
            dateTime = parsedDt,
        )
    }

    private fun mapToPrice(priceResponseModel: PriceResponseModel) = Price(
        id = priceResponseModel.id.orEmpty(),
        name = priceResponseModel.name.orEmpty(),
        text = priceResponseModel.name.orEmpty(),
        price = priceResponseModel.price ?: 0,
        currency = priceResponseModel.currency ?: PriceCurrency.RUB,
    )

    private fun mapToSocialMedia(socialMediaResponseModel: SocialMediaResponseModel): SocialMedia =
        SocialMedia(
            link = socialMediaResponseModel.link.orEmpty(),
            type = socialMediaResponseModel.type ?: SocialMediaType.INSTAGRAM,
        )
}

data class SocialMediaResponseModel(
    val link: String? = null,
    val type: SocialMediaType? = null,
)

data class ReviewResponseModel(
    val name: String? = null,
    val surname: String? = null,
    val avatarUrl: String? = null,
    val dateTime: String? = null,
)

data class PriceResponseModel(
    val id: String? = null,
    var name: String? = null,
    var text: String? = null,
    var price: Int? = null,
    val currency: PriceCurrency? = null,
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