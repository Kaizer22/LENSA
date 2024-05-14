package ru.arinae_va.lensa.data.repositroy

import ru.arinae_va.lensa.data.datasource.remote.IReviewDataSource
import ru.arinae_va.lensa.domain.model.user.Review
import ru.arinae_va.lensa.domain.repository.IAuthRepository
import ru.arinae_va.lensa.domain.repository.IReviewRepository
import javax.inject.Inject

class ReviewRepository @Inject constructor(
    private val reviewDataSource: IReviewDataSource,
    private val authRepository: IAuthRepository,
): IReviewRepository {

    override suspend fun getReviewsByProfileId(profileId: String): List<Review> =
        reviewDataSource.getReviewsByProfileId(profileId)

    override suspend fun upsertReview(targetProfileId: String, review: Review) {
        reviewDataSource.upsertReview(
            targetProfileId = targetProfileId,
            currentUserId = authRepository.currentUserId().orEmpty(),
            review = review,
        )
    }

    override suspend fun deleteReview(targetProfileId: String) {
        reviewDataSource.deleteReview(
            targetProfileId = targetProfileId,
            currentUserId = authRepository.currentUserId().orEmpty(),
        )
    }
}