package ru.arinae_va.lensa.domain.repository

import ru.arinae_va.lensa.domain.model.user.Review

interface IReviewRepository {

    suspend fun getReviewsByProfileId(profileId: String): List<Review>

    suspend fun upsertReview(targetProfileId: String, review: Review)

    suspend fun deleteReview(targetProfileId: String)
}