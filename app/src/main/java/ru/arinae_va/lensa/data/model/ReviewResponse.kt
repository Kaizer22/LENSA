package ru.arinae_va.lensa.data.model

import ru.arinae_va.lensa.domain.model.Review

data class ReviewResponse(
    val authorId: String? = null,
    val name: String? = null,
    val surname: String? = null,
    val avatarUrl: String? = null,
    val dateTime: String? = null,
    val text: String? = null,
    val rating: Float? = null,
)

fun Review.toReviewResponse() = ReviewResponse(
    authorId = authorId,
    name = name,
    surname = surname,
    avatarUrl = avatarUrl,
    text = text,
    rating = rating,
    dateTime = dateTime.toString(),
)