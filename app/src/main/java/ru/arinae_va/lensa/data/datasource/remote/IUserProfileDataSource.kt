package ru.arinae_va.lensa.data.datasource.remote

import android.net.Uri
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import ru.arinae_va.lensa.data.model.UserProfileResponse
import ru.arinae_va.lensa.domain.model.FeedFilter
import ru.arinae_va.lensa.domain.model.OrderType
import ru.arinae_va.lensa.domain.model.UserProfileModel
import ru.arinae_va.lensa.domain.model.UserProfileType
import javax.inject.Inject

interface IUserProfileDataSource {

    fun checkUid(uid: String, onCheckResult: (isNew: Boolean) -> Unit)

    suspend fun upsertProfile(profile: UserProfileModel)

    suspend fun uploadAvatarImage(profileUid: String, imageUri: Uri): Boolean

    // TODO возврат результата операции, а не просто bool
    suspend fun uploadPortfolioImage(profileUid: String, imageUri: Uri): Boolean
    suspend fun setProfileAvatar(profileUid: String, downloadUrl: String): Boolean
    suspend fun addPortfolioPicture(profileUid: String, downloadUrl: String): Boolean
    suspend fun deleteProfile(profileUid: String): Boolean

    suspend fun getFeed(
        currentUserId: String,
        feedFilter: FeedFilter?,
    ): List<UserProfileModel>

    suspend fun getProfileById(profileUid: String?): UserProfileModel
    suspend fun getProfilesByIds(profileIds: List<String>): List<UserProfileModel>
}

private const val PROFILES_COLLECTION = "profile"
private const val USER_PROFILES_COLLECTION = "userProfiles"

private const val AVATAR_FIELD = "avatarUrl"
private const val PORTFOLIO_PICTURES_FIELD = "portfolioUrls"
private const val USER_ID_FIELD = "userId"
private const val PROFILE_ID_FIELD = "profileId"
private const val PROFILE_TYPE_FIELD = "type"
private const val SPECIALIZATION_FIELD = "specialization"
private const val COUNTRY_FIELD = "country"
private const val RATING_FIELD = "rating"
private const val MIN_PRICE_FIELD = "minPrice"
private const val MAX_PRICE_FIELD = "maxPrice"
private const val REVIEWS_FIELD = "reviews"

private const val AVATARS_STORAGE_ROOT_FOLDER = "avatars/"
private const val AVATAR_STORAGE_FILE_NAME = "avatar"
private const val PORTFOLIOS_STORAGE_ROOT_FOLDER = "portfolios/"

class FirebaseUserProfileDataSource @Inject constructor(
    private val database: FirebaseFirestore,
    private val firebaseStorage: StorageReference,
) : IUserProfileDataSource {

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

    override suspend fun upsertProfile(profile: UserProfileModel) {
        val ref = database.collection(PROFILES_COLLECTION)
            .document(profile.userId)
            .collection(USER_PROFILES_COLLECTION)
            .document(profile.profileId)
        ref.set(profile).await()
    }

    override suspend fun setProfileAvatar(profileUid: String, downloadUrl: String
    ): Boolean {
        var res = false
        val ref = database.collection(PROFILES_COLLECTION).document(profileUid)
        ref.update(AVATAR_FIELD, downloadUrl)
            .addOnSuccessListener { res = true }
            .await()
        return res
    }

    override suspend fun addPortfolioPicture(
        profileUid: String,
        downloadUrl: String
    ): Boolean {
        var res = false
        val ref = database.collection(PROFILES_COLLECTION).document(profileUid)
        ref.update(PORTFOLIO_PICTURES_FIELD, FieldValue.arrayUnion(downloadUrl))
            .addOnSuccessListener { res = true }
            .await()
        return res
    }

    override suspend fun deleteProfile(profileUid: String): Boolean {
        var res = false
        database.collection(PROFILES_COLLECTION).document(profileUid).delete()
            .addOnSuccessListener { res = true }
            .await()
        return res
    }

    override suspend fun getFeed(
        currentUserId: String,
        feedFilter: FeedFilter?,
    ): List<UserProfileModel> {
        var result = listOf<UserProfileModel>()
        var baseQuery = database.collection(PROFILES_COLLECTION)
            .whereEqualTo(PROFILE_TYPE_FIELD, UserProfileType.SPECIALIST.name)
            .whereNotEqualTo(USER_ID_FIELD, currentUserId)
        // TODO caching
        baseQuery.get()
            .addOnSuccessListener {
                result = it.documents.mapNotNull { doc ->
                    doc.toObject<UserProfileResponse>()?.mapToSpecialistModel()
                }
                // TODO организовать иерархию сущностей на бэке для фильтрации
                // TODO pagination
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
        feedFilter: FeedFilter,
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

    override suspend fun uploadAvatarImage(profileUid: String, imageUri: Uri): Boolean {
        val avatarFolderRef = firebaseStorage.child(
            AVATARS_STORAGE_ROOT_FOLDER + profileUid + "/" + imageUri.lastPathSegment
        )
        val imageRef = avatarFolderRef.child("$AVATAR_STORAGE_FILE_NAME.jpg")
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
                return setProfileAvatar(profileUid, downloadUrl.toString())
            } else {
                false
            }
        } else {
            false
        }
    }

    override suspend fun uploadPortfolioImage(profileUid: String, imageUri: Uri): Boolean {
        val imageRef = firebaseStorage.child(
            PORTFOLIOS_STORAGE_ROOT_FOLDER + profileUid + "/" +
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
                return addPortfolioPicture(profileUid, downloadUrl.toString())
            } else {
                false
            }
        } else {
            false
        }
    }

    override suspend fun getProfileById(profileUid: String?) = database
        .collection(PROFILES_COLLECTION)
        .whereEqualTo(PROFILE_ID_FIELD, profileUid)
        .limit(1)
        .get()
        .await()
        .documents[0].toObject(UserProfileResponse::class.java)
        ?.mapToSpecialistModel()
        ?: UserProfileModel.EMPTY

    override suspend fun getProfilesByIds(profileIds: List<String>): List<UserProfileModel> =
        database
            .collection(PROFILES_COLLECTION)
            .whereIn(PROFILE_ID_FIELD, profileIds)
            .get()
            .await()
            .documents.map { it.toObject(UserProfileResponse::class.java) }
            .mapNotNull { it?.mapToSpecialistModel() }


}

data class FeedbackModel(
    val userUid: String?,
    val text: String,
    val timestamp: String,
)
