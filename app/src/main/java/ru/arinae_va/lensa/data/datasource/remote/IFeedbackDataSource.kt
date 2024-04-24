package ru.arinae_va.lensa.data.datasource.remote

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import javax.inject.Inject

interface IFeedbackDataSource {
    suspend fun sendFeedback(profileUid: String?, text: String)
}

private const val FEEDBACK_COLLECTION = "feedback"
class FirebaseFeedbackDataSource @Inject constructor(
    database: FirebaseFirestore,
): IFeedbackDataSource {
    private val feedbacks = database.collection(FEEDBACK_COLLECTION)
    override suspend fun sendFeedback(profileUid: String?, text: String) {
        feedbacks.add(
            FeedbackModel(
                userUid = profileUid,
                text = text,
                timestamp = LocalDateTime.now().toString()
            )
        ).await()
    }
}