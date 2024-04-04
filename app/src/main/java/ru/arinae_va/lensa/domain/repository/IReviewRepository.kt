package ru.arinae_va.lensa.domain.repository

import ru.arinae_va.lensa.domain.model.Review

interface IReviewRepository {
    suspend fun upsertReview(targetProfileId: String, review: Review)

    suspend fun deleteReview(targetProfileId: String)
}