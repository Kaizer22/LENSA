package ru.arinae_va.lensa.data.model

import ru.arinae_va.lensa.domain.model.Price
import ru.arinae_va.lensa.domain.model.PriceCurrency
import ru.arinae_va.lensa.domain.model.Review
import ru.arinae_va.lensa.domain.model.SocialMedia
import ru.arinae_va.lensa.domain.model.UserProfileModel
import ru.arinae_va.lensa.domain.model.UserProfileType
import ru.arinae_va.lensa.presentation.feature.feed.compose.SocialMediaType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UserProfileResponse(
    val userId: String? = null,
    val profileId: String? = null,
    val type: String? = null,
    val name: String? = null,
    val surname: String? = null,
    val specialization: String? = null,
    val rating: Float? = null,
    val avatarUrl: String? = null,
    val country: String? = null,
    val city: String? = null,
    val personalSite: String? = null,
    val phoneNumber: String? = null,
    val email: String? = null,
    val socialMedias: List<SocialMediaResponse>? = null,
    val about: String? = null,
    val portfolioUrls: List<String>? = null,
    val prices: List<PriceResponse>? = null,
    val minimalPrice: Int? = null,
    val maximalPrice: Int? = null,
    val reviews: List<ReviewResponse>? = null,
) {
    fun mapToSpecialistModel(): UserProfileModel = UserProfileModel(
        userId = userId.orEmpty(),
        profileId = profileId.orEmpty(),
        type = type?.let { UserProfileType.valueOf(it) } ?: UserProfileType.CUSTOMER,
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
        minimalPrice = minimalPrice,
        maximalPrice = maximalPrice,
        phoneNumber = phoneNumber.orEmpty(),
        reviews = reviews.orEmpty().map {
            mapToReview(it)
        },
    )

    private fun mapToReview(reviewResponseModel: ReviewResponse): Review {
        val parsedDt = LocalDateTime.parse(
            reviewResponseModel.dateTime,
            DateTimeFormatter.ISO_DATE_TIME
        )
        return Review(
            authorId = reviewResponseModel.authorId.orEmpty(),
            name = reviewResponseModel.name.orEmpty(),
            surname = reviewResponseModel.surname.orEmpty(),
            avatarUrl = reviewResponseModel.avatarUrl.orEmpty(),
            rating = reviewResponseModel.rating ?: 0f,
            text = reviewResponseModel.text.orEmpty(),
            dateTime = parsedDt,
        )
    }

    private fun mapToPrice(priceResponseModel: PriceResponse) = Price(
        id = priceResponseModel.id.orEmpty(),
        name = priceResponseModel.name.orEmpty(),
        text = priceResponseModel.text.orEmpty(),
        price = priceResponseModel.price ?: 0,
        currency = priceResponseModel.currency ?: PriceCurrency.RUB,
    )

    private fun mapToSocialMedia(socialMediaResponseModel: SocialMediaResponse): SocialMedia =
        SocialMedia(
            link = socialMediaResponseModel.link.orEmpty(),
            type = socialMediaResponseModel.type ?: SocialMediaType.INSTAGRAM,
        )
}