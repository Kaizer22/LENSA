package ru.arinae_va.lensa.data.datasource.remote

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import ru.arinae_va.lensa.data.model.toReviewResponse
import ru.arinae_va.lensa.domain.model.Review
import javax.inject.Inject

interface IReviewDataSource {
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

class FirebaseReviewDataSource @Inject constructor(
    private val database: FirebaseFirestore,
) : IReviewDataSource {

    override suspend fun upsertReview(
        targetProfileId: String,
        currentUserId: String,
        review: Review,
    ): Boolean {
        var res = true
        val mappedReview = review.toReviewResponse()
        database.collection(REVIEWS_COLLECTION)
            .document("${currentUserId}_${targetProfileId}")
            .set(mappedReview)
            .addOnCanceledListener { res = false }
            .addOnFailureListener { res = false }
            .await()

        return res
    }

    override suspend fun deleteReview(targetProfileId: String, currentUserId: String) {
        database.collection(REVIEWS_COLLECTION)
            .document("${currentUserId}_${targetProfileId}").delete()
            .await()
    }
}