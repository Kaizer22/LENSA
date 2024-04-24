package ru.arinae_va.lensa.data.datasource.remote

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import ru.arinae_va.lensa.data.model.ReviewResponse
import ru.arinae_va.lensa.data.model.toReview
import ru.arinae_va.lensa.data.model.toReviewResponse
import ru.arinae_va.lensa.domain.model.Review
import javax.inject.Inject

interface IReviewDataSource {

    suspend fun getReviewsByProfileId(
        profileId: String
    ): List<Review>

    suspend fun upsertReview(
        targetProfileId: String,
        currentUserId: String,
        review: Review,
    ): Boolean

    suspend fun deleteReview(
        targetProfileId: String,
        currentUserId: String,
    )
}

private const val REVIEWS_COLLECTION = "review"

private const val REVIEWS_PROFILE_ID_FIELD = "profileId"

class FirebaseReviewDataSource @Inject constructor(
    database: FirebaseFirestore,
) : IReviewDataSource {
    private val reviews = database.collection(REVIEWS_COLLECTION)
    override suspend fun getReviewsByProfileId(profileId: String): List<Review> =
        reviews.whereEqualTo(REVIEWS_PROFILE_ID_FIELD, profileId)
            .get()
            .await()
            .documents.map {
                it.toObject(ReviewResponse::class.java)
            }
            .mapNotNull { it?.toReview() }


    override suspend fun upsertReview(
        targetProfileId: String,
        currentUserId: String,
        review: Review,
    ): Boolean {
        var res = true
        val mappedReview = review.toReviewResponse()
        reviews.document("${currentUserId}_${targetProfileId}")
            .set(mappedReview)
            .addOnCanceledListener { res = false }
            .addOnFailureListener { res = false }
            .await()

        return res
    }

    override suspend fun deleteReview(targetProfileId: String, currentUserId: String) {
        reviews.document("${currentUserId}_${targetProfileId}").delete()
            .await()
    }
}