package ru.arinae_va.lensa.data.model

import ru.arinae_va.lensa.domain.model.Review
import ru.arinae_va.lensa.utils.parseIsoDatetime

data class ReviewResponse(
    val authorId: String? = null,
    val profileId: String? = null,
    val name: String? = null,
    val surname: String? = null,
    val avatarUrl: String? = null,
    val dateTime: String? = null,
    val text: String? = null,
    val rating: Float? = null,
)

fun Review.toReviewResponse() = ReviewResponse(
    profileId = profileId,
    authorId = authorId,
    name = name,
    surname = surname,
    avatarUrl = avatarUrl,
    text = text,
    rating = rating,
    dateTime = dateTime.toString(),
)

fun ReviewResponse.toReview() = Review(
    authorId = authorId.orEmpty(),
    name = name.orEmpty(),
    surname = surname.orEmpty(),
    avatarUrl = avatarUrl.orEmpty(),
    rating = rating ?: 0f,
    text = text.orEmpty(),
    dateTime = parseIsoDatetime(dateTime.orEmpty()),
    profileId = profileId.orEmpty(),
)
