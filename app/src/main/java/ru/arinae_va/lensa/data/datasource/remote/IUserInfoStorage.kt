package ru.arinae_va.lensa.data.datasource.remote

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import ru.arinae_va.lensa.data.model.UserProfileResponse
import ru.arinae_va.lensa.data.model.toReviewResponse
import ru.arinae_va.lensa.domain.model.FeedFilter
import ru.arinae_va.lensa.domain.model.OrderType
import ru.arinae_va.lensa.domain.model.Review
import ru.arinae_va.lensa.domain.model.UserProfileModel
import ru.arinae_va.lensa.domain.model.UserProfileType
import java.time.LocalDateTime
import javax.inject.Inject

interface IUserInfoStorage {

    fun checkUid(uid: String, onCheckResult: (isNew: Boolean) -> Unit)

    suspend fun createProfile(profile: UserProfileModel)
    suspend fun updateProfile(profile: UserProfileModel)

    suspend fun uploadAvatarImage(userUid: String, imageUri: Uri): Boolean

    // TODO возврат результата операции, а не просто bool
    suspend fun uploadPortfolioImage(userUid: String, imageUri: Uri): Boolean
    suspend fun setUserAvatar(userUid: String, downloadUrl: String): Boolean
    suspend fun addPortfolioPicture(userUid: String, downloadUrl: String): Boolean
    suspend fun deleteAccount(userUid: String): Boolean

    suspend fun getFeed(feedFilter: FeedFilter?): List<UserProfileModel>
    suspend fun sendFeedback(userUid: String?, text: String)
    suspend fun getProfileById(userUid: String?): UserProfileModel

    suspend fun postReview(targetUserId: String, review: Review): Boolean
}

private const val PROFILES_COLLECTION = "profile"
private const val FEEDBACK_COLLECTION = "feedback"

private const val AVATAR_FIELD = "avatarUrl"
private const val PORTFOLIO_PICTURES_FIELD = "portfolioUrls"
private const val ID_FIELD = "id"
private const val PROFILE_TYPE_FIELD = "type"
private const val SPECIALIZATION_FIELD = "specialization"
private const val COUNTRY_FIELD = "country"
private const val RATING_FIELD = "rating"
private const val MIN_PRICE_FIELD = "minPrice"
private const val MAX_PRICE_FIELD = "maxPrice"
private const val REVIEWS_FIELD = "reviews"

private const val AVATARS_STORAGE_ROOT_FOLDER = "avatars/"
private const val PORTFOLIOS_STORAGE_ROOT_FOLDER = "portfolios/"

class FirebaseUserInfoStorage @Inject constructor() : IUserInfoStorage {
    private val database: FirebaseFirestore = Firebase.firestore
    private val firebaseStorage: StorageReference = Firebase.storage.reference

    override fun checkUid(uid: String, onCheckResult: (isNew: Boolean) -> Unit) {
        database.collection(PROFILES_COLLECTION).document(uid).get()
            .addOnSuccessListener { data ->
                onCheckResult(data.data == null)
            }
            .addOnFailureListener {
                onCheckResult(true)
            }
            .addOnCanceledListener {
                onCheckResult(true)
            }
    }

    override suspend fun createProfile(profile: UserProfileModel) {
        val ref = database.collection(PROFILES_COLLECTION).document(profile.id)
        ref.set(profile).await()
    }

    override suspend fun updateProfile(profile: UserProfileModel) {
        TODO("Not yet implemented")
    }

    override suspend fun setUserAvatar(userUid: String, downloadUrl: String): Boolean {
        var res = false
        val ref = database.collection(PROFILES_COLLECTION).document(userUid)
        ref.update(AVATAR_FIELD, downloadUrl)
            .addOnSuccessListener { res = true }
            .await()
        return res
    }

    override suspend fun addPortfolioPicture(userUid: String, downloadUrl: String): Boolean {
        var res = false
        val ref = database.collection(PROFILES_COLLECTION).document(userUid)
        ref.update(PORTFOLIO_PICTURES_FIELD, FieldValue.arrayUnion(downloadUrl))
            .addOnSuccessListener { res = true }
            .await()
        return res
    }

    override suspend fun deleteAccount(userUid: String): Boolean {
        var res = false
        database.collection(PROFILES_COLLECTION).document(userUid).delete()
            .addOnSuccessListener { res = true }
            .await()
        return res
    }

    override suspend fun getFeed(feedFilter: FeedFilter?): List<UserProfileModel> {
        var result = listOf<UserProfileModel>()
        var baseQuery = database.collection(PROFILES_COLLECTION)
            .whereEqualTo(PROFILE_TYPE_FIELD, UserProfileType.SPECIALIST.name)
            .whereNotEqualTo(ID_FIELD, Firebase.auth.currentUser?.uid)
        // TODO caching
        baseQuery.get()
            .addOnSuccessListener {
                result = it.documents.mapNotNull { doc ->
                    doc.toObject<UserProfileResponse>()?.mapToSpecialistModel()
                }
                // TODO filter on backend + pagination
                feedFilter?.let { filter ->
                    result = applyFilter(result, filter)
                }
            }
            .addOnFailureListener {
                result = listOf()
            }
            .await()
        return result
    }

    private fun applyFilter(
        list: List<UserProfileModel>,
        feedFilter: FeedFilter
    ): List<UserProfileModel> {
        with(feedFilter) {
            var result = list
                .filter {
                    if (specialization.isNotEmpty()) it.specialization == specialization else true
                }
                .filter {
                    if (city.isNotEmpty()) it.city.contains(city, ignoreCase = true) else true
                }
                .filter { if (country.isNotEmpty()) it.country == country else true }
                .filter {
                    (it.maximalPrice ?: Int.MAX_VALUE) >= priceFrom &&
                            (it.minimalPrice ?: Int.MIN_VALUE) <= priceTo
                }

            if (searchQuery.isNotEmpty())
                result = search(searchQuery)

            result = when (order) {
                OrderType.RATING_ASC ->
                    result.sortedBy { it.rating }

                OrderType.RATING_DESC ->
                    result.sortedByDescending { it.rating }
            }

            return result
        }
    }

    private fun search(searchQuery: String): List<UserProfileModel> {
        TODO("Not yet implemented")
    }

    override suspend fun sendFeedback(userUid: String?, text: String) {
        database.collection(FEEDBACK_COLLECTION).add(
            FeedbackModel(
                userUid = userUid,
                text = text,
                timestamp = LocalDateTime.now().toString()
            )
        ).await()
    }

    override suspend fun uploadAvatarImage(userUid: String, imageUri: Uri): Boolean {
        val imageRef = firebaseStorage.child(
            AVATARS_STORAGE_ROOT_FOLDER + userUid + "/" + imageUri.lastPathSegment
        )
        val uploadTask = imageRef.putFile(imageUri)

        var uploadResultSuccess = false
        uploadTask.addOnSuccessListener { uploadResultSuccess = true }
            .await()

        return if (uploadResultSuccess) {
            var downloadUrlResultSuccess = false
            val downloadUrl = imageRef.downloadUrl
                .addOnSuccessListener { downloadUrlResultSuccess = true }
                .await()
            if (downloadUrlResultSuccess) {
                return setUserAvatar(userUid, downloadUrl.toString())
            } else {
                false
            }
        } else {
            false
        }
//        uploadTask.addOnSuccessListener {
//
//            downloadUrl.addOnSuccessListener {
//                photo.remoteUri = it.toString()
//                // update our Cloud Firestore with the public image URI.
//                updatePhotoDatabase(specimen, photo)
//            }
//
//        }
//        uploadTask.addOnFailureListener {
//            Log.e(TAG, it.message)
//        }
    }

    override suspend fun uploadPortfolioImage(userUid: String, imageUri: Uri): Boolean {
        val imageRef = firebaseStorage.child(
            PORTFOLIOS_STORAGE_ROOT_FOLDER + userUid + "/" +
                    imageUri.lastPathSegment
        )
        val uploadTask = imageRef.putFile(imageUri)

        var uploadResultSuccess = false
        uploadTask.addOnSuccessListener { uploadResultSuccess = true }
            .await()

        return if (uploadResultSuccess) {
            var downloadUrlResultSuccess = false
            val downloadUrl = imageRef.downloadUrl
                .addOnSuccessListener { downloadUrlResultSuccess = true }
                .await()
            if (downloadUrlResultSuccess) {
                return addPortfolioPicture(userUid, downloadUrl.toString())
            } else {
                false
            }
        } else {
            false
        }
    }

    override suspend fun getProfileById(userUid: String?) = database
        .collection(PROFILES_COLLECTION)
        .whereEqualTo(ID_FIELD, userUid)
        .limit(1)
        .get()
        .await()
        .documents[0].toObject(UserProfileResponse::class.java)
        ?.mapToSpecialistModel()
        ?: UserProfileModel.EMPTY

    override suspend fun postReview(targetUserId: String, review: Review): Boolean {
        var res = true
        val mappedReview = review.toReviewResponse()
        database.collection(PROFILES_COLLECTION)
            .document(targetUserId).update(
                REVIEWS_FIELD, FieldValue.arrayUnion(mappedReview)
            )
            .addOnCanceledListener { res = false }
            .addOnFailureListener { res = false }
            .await()

        return res
    }
}

data class FeedbackModel(
    val userUid: String?,
    val text: String,
    val timestamp: String,
)
