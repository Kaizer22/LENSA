package ru.arinae_va.lensa.domain.repository

import android.net.Uri
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import ru.arinae_va.lensa.data.model.FavouriteFolder
import ru.arinae_va.lensa.domain.model.FeedFilter
import ru.arinae_va.lensa.domain.model.Review
import ru.arinae_va.lensa.domain.model.UserProfileModel

abstract class IUserInfoRepository {

    protected var currentUserProfile: UserProfileModel? = null

    var currentUserName: String? = null
        get() = currentUserProfile?.name
        private set

    var currentUserSpecialization: String? = null
        get() = currentUserProfile?.specialization
        private set

    var currentUserSurname: String? = null
        get() = currentUserProfile?.surname
        private set

    var currentUserAvatarUrl: String? = null
        get() = currentUserProfile?.avatarUrl
        private set
    abstract fun currentUserId(): String?

    abstract fun verifyPhoneNumber(
        phoneNumber: String,
        onSignInCompleted: (userUid: String) -> Unit,
        onSignUpCompleted: (userUid: String) -> Unit,
        onVerificationFailed: (String) -> Unit,
        onCodeSent: (String, PhoneAuthProvider.ForceResendingToken) -> Unit,
    )

    abstract fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        onSignUpSuccess: (userUid: String) -> Unit,
        onSignInSuccess: (userUid: String) -> Unit,
        onSignInFailed: () -> Unit,
    )

    abstract suspend fun logIn(currentUserId: String)

    abstract fun logOut()

    abstract suspend fun deleteAccount()

    abstract suspend fun upsertProfile(
        model: UserProfileModel,
        avatarUri: Uri? = null,
        portfolioUris: List<Uri>? = null,
        isNewUser: Boolean = false,
    )

    abstract suspend fun getFeed(
        feedFilter: FeedFilter?
    ): List<UserProfileModel> // by filter

    abstract suspend fun postReview(targetUserId: String, review: Review)

    abstract suspend fun addFavourite(userId: String, folderName: String)

    abstract suspend fun removeFavourite(userId: String, folderName: String)

    abstract suspend fun getFavourites(): List<FavouriteFolder>

    abstract suspend fun sendFeedback(userUid: String?, text: String)
    abstract suspend fun getProfileById(userUid: String): UserProfileModel

    abstract suspend fun getProfilesByIds(userIds: List<String>): List<UserProfileModel>
}