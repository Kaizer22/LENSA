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
import java.time.LocalDateTime
import javax.inject.Inject

interface IUserProfileDataSource {

    suspend fun isNewUser(userId: String): Boolean

    suspend fun upsertProfile(profile: UserProfileModel)

    suspend fun uploadAvatarImage(profileUid: String, imageUri: Uri): Boolean

    suspend fun uploadPortfolioImage(profileUid: String, imageUri: Uri): Boolean
    suspend fun setProfileAvatar(profileUid: String, downloadUrl: String): Boolean
    suspend fun addPortfolioPicture(profileUid: String, downloadUrl: String): Boolean
    suspend fun deleteProfile(profileUid: String): Boolean

    suspend fun getFeed(
        currentUserId: String,
        feedFilter: FeedFilter?,
    ): List<UserProfileModel>

    fun applyFilter(profiles: List<UserProfileModel>?, filter: FeedFilter): List<UserProfileModel>

    suspend fun getProfileById(profileUid: String?): UserProfileModel
    suspend fun getProfilesByIds(profileIds: List<String>): List<UserProfileModel>

    suspend fun getProfilesByUserId(userId: String): List<UserProfileModel>
    suspend fun updateRating(rating: Float, profileId: String): Boolean
}

private const val PROFILES_COLLECTION = "profile"

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

private const val FEED_CACHE_TIMEOUT = 10L // seconds

class FirebaseUserProfileDataSource @Inject constructor(
    private val database: FirebaseFirestore,
    private val firebaseStorage: StorageReference,
) : IUserProfileDataSource {

    private val profiles = database.collection(PROFILES_COLLECTION)

    private var feedCache: List<UserProfileModel> = emptyList()
    private var feedUpdateTime: LocalDateTime = LocalDateTime.MIN

    override suspend fun isNewUser(userId: String) =
        getProfilesByUserId(userId).isEmpty()

    override suspend fun upsertProfile(profile: UserProfileModel) {
        profiles.document(profile.profileId)
            .set(profile)
            .await()
    }

    override suspend fun setProfileAvatar(
        profileUid: String, downloadUrl: String,
    ): Boolean {
        var res = false
        profiles.document(profileUid)
            .update(AVATAR_FIELD, downloadUrl)
            .addOnSuccessListener { res = true }
            .await()
        return res
    }

    override suspend fun addPortfolioPicture(
        profileUid: String,
        downloadUrl: String,
    ): Boolean {
        var res = false
        profiles.document(profileUid)
            .update(PORTFOLIO_PICTURES_FIELD, FieldValue.arrayUnion(downloadUrl))
            .addOnSuccessListener { res = true }
            .await()
        return res
    }

    override suspend fun deleteProfile(profileUid: String): Boolean {
        var res = false
        profiles.document(profileUid)
            .delete()
            .addOnSuccessListener { res = true }
            .await()
        return res
    }

    override suspend fun getFeed(
        currentUserId: String,
        feedFilter: FeedFilter?,
    ): List<UserProfileModel> {
        val currentTime = LocalDateTime.now()
        if (feedUpdateTime.plusSeconds(FEED_CACHE_TIMEOUT) < currentTime) {
            var result = listOf<UserProfileModel>()
            val baseQuery = profiles
                .whereEqualTo(PROFILE_TYPE_FIELD, UserProfileType.SPECIALIST.name)
                .whereNotEqualTo(USER_ID_FIELD, currentUserId)
            val docs = baseQuery.get().await()
            docs?.let {
                result = it.documents.mapNotNull { doc ->
                    doc.toObject<UserProfileResponse>()?.mapToUserProfileModel()
                }
                // TODO организовать иерархию сущностей на бэке для фильтрации
//                // TODO pagination
                feedFilter?.let { filter ->
                    result = applyFilter(result, filter)
                }
            } ?: run {}
            return result
        } else {
            return feedCache
        }
    }

    override fun applyFilter(
        profiles: List<UserProfileModel>?,
        filter: FeedFilter,
    ): List<UserProfileModel> {
        with(filter) {
            var result = (profiles ?: feedCache)
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
                result = search(result, searchQuery)

            result = when (order) {
                OrderType.RATING_ASC ->
                    result.sortedBy { it.rating }

                OrderType.RATING_DESC ->
                    result.sortedByDescending { it.rating }
            }

            return result
        }
    }

    private fun search(
        profiles: List<UserProfileModel>,
        searchQuery: String,
    ): List<UserProfileModel> {
        val query = searchQuery.lowercase()
        return profiles.filter {
            it.specialization.lowercase().contains(query) ||
                    ("${it.surname} ${it.name}".lowercase()).contains(query)
                    || it.about.lowercase().contains(query)
        }
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

    override suspend fun getProfileById(profileUid: String?) = profiles
        .document(profileUid.orEmpty())
        .get()
        .await()
        .toObject(UserProfileResponse::class.java)
        ?.mapToUserProfileModel()
        ?: UserProfileModel.EMPTY

    override suspend fun getProfilesByIds(profileIds: List<String>): List<UserProfileModel> =
        profiles.whereIn(PROFILE_ID_FIELD, profileIds)
            .get()
            .await()
            .documents.map { it.toObject(UserProfileResponse::class.java) }
            .mapNotNull { it?.mapToUserProfileModel() }

    override suspend fun getProfilesByUserId(userId: String): List<UserProfileModel> =
        profiles.whereEqualTo(USER_ID_FIELD, userId)
            .get()
            .await()
            .documents.map { it.toObject(UserProfileResponse::class.java) }
            .mapNotNull { it?.mapToUserProfileModel() }

    override suspend fun updateRating(rating: Float, profileId: String): Boolean {
        profiles.document(profileId)
            .update(RATING_FIELD, rating)
            .await()
        return true
    }
}

data class FeedbackModel(
    val userUid: String?,
    val text: String,
    val timestamp: String,
)
