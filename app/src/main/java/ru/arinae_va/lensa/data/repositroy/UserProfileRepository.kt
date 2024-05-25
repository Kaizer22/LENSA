package ru.arinae_va.lensa.data.repositroy

import android.net.Uri
import android.webkit.URLUtil.isHttpsUrl
import ru.arinae_va.lensa.data.datasource.remote.IUserProfileDataSource
import ru.arinae_va.lensa.domain.model.user.FeedFilter
import ru.arinae_va.lensa.domain.model.user.UserProfileModel
import ru.arinae_va.lensa.domain.repository.IUserProfileRepository
import java.util.UUID
import javax.inject.Inject

class UserProfileRepository @Inject constructor(
    private val userInfoStorage: IUserProfileDataSource,
) : IUserProfileRepository {

    private var currentUserProfile: UserProfileModel? = null
    override fun currentUserProfile(): UserProfileModel? = currentUserProfile

    override fun currentProfileId(): String? = currentUserProfile?.profileId
    override suspend fun addProfileToBlackList(blockedProfileId: String) {
        currentUserProfile?.let {
            userInfoStorage.addProfileToBlackList(it.profileId, blockedProfileId)
        }
    }

    override suspend fun removeProfileFromBlackList(profileId: String) {
        currentUserProfile?.let {
            userInfoStorage.removeProfileFromBlackList(it.profileId, profileId)
        }
    }

    override suspend fun deleteProfile() {
        currentUserProfile?.let {
            userInfoStorage.deleteProfile(it.profileId)
        }
    }

    override suspend fun upsertProfile(
        model: UserProfileModel,
        avatarUri: Uri?,
        portfolioUris: List<Uri>?,
        isNewUser: Boolean,
    ): String? {
        var resultId: String? = null
        val processedModel = if (isNewUser) {
            val newProfileUuid = UUID.randomUUID().toString()
            resultId = newProfileUuid
            model.copy(profileId = newProfileUuid)
        } else model
        userInfoStorage.upsertProfile(profile = processedModel)
        // TODO use WorkManager to upload images
        avatarUri?.let {
            if (!isHttpsUrl(it.toString())) {
                userInfoStorage.uploadAvatarImage(processedModel.profileId, it)
            }
        }
        portfolioUris?.let {
            it.forEach { imageUri ->
                if (!isHttpsUrl(imageUri.toString())) {
                    userInfoStorage.uploadPortfolioImage(processedModel.profileId, imageUri)
                }
            }
        }
        return resultId
    }

    override suspend fun updateRating(rating: Float, profileId: String){
        userInfoStorage.updateRating(rating, profileId)
    }

    // TODO caching
    override suspend fun getFeed(feedFilter: FeedFilter?): List<UserProfileModel> {
        currentUserProfile?.let {
            return userInfoStorage.getFeed(it.userId, feedFilter)
        }
        return emptyList()
    }

    override suspend fun getProfileById(profileUid: String): UserProfileModel =
        userInfoStorage.getProfileById(profileUid)

    override suspend fun getProfilesByIds(profileIds: List<String>): List<UserProfileModel> =
        userInfoStorage.getProfilesByIds(profileIds)

    override suspend fun getProfilesByUserId(userId: String): List<UserProfileModel> =
        userInfoStorage.getProfilesByUserId(userId)

    override fun clearCurrentUser() {
        currentUserProfile = null
    }

    override suspend fun setCurrentUser(currentProfileId: String) {
        currentUserProfile = getProfileById(currentProfileId)
    }
}