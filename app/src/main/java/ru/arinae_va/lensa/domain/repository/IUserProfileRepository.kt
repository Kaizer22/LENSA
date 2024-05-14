package ru.arinae_va.lensa.domain.repository

import android.net.Uri
import ru.arinae_va.lensa.domain.model.user.FeedFilter
import ru.arinae_va.lensa.domain.model.user.UserProfileModel

interface IUserProfileRepository {

    fun currentUserProfile(): UserProfileModel?
    fun currentProfileId(): String?

    suspend fun addProfileToBlackList(blockedProfileId: String)

    suspend fun removeProfileFromBlackList(profileId: String)

    suspend fun deleteProfile()

    suspend fun upsertProfile(
        model: UserProfileModel,
        avatarUri: Uri? = null,
        portfolioUris: List<Uri>? = null,
        isNewUser: Boolean = false,
    ): String?

    suspend fun updateRating(
        rating: Float,
        profileId: String,
    )

    suspend fun getFeed(
        feedFilter: FeedFilter?
    ): List<UserProfileModel> // by filter

    suspend fun getProfileById(profileUid: String): UserProfileModel

    suspend fun getProfilesByIds(profileIds: List<String>): List<UserProfileModel>

    suspend fun getProfilesByUserId(userId: String): List<UserProfileModel>
    fun clearCurrentUser()
    suspend fun setCurrentUser(currentProfileId: String)
}