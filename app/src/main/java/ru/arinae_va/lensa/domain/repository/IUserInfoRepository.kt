package ru.arinae_va.lensa.domain.repository

import android.net.Uri
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import ru.arinae_va.lensa.data.model.FavouriteFolder
import ru.arinae_va.lensa.domain.model.FeedFilter
import ru.arinae_va.lensa.domain.model.Review
import ru.arinae_va.lensa.domain.model.UserProfileModel

interface IUserInfoRepository {

    var currentUserProfile: UserProfileModel?

    fun verifyPhoneNumber(
        phoneNumber: String,
        onSignInCompleted: (userUid: String) -> Unit,
        onSignUpCompleted: (userUid: String) -> Unit,
        onVerificationFailed: (String) -> Unit,
        onCodeSent: (String, PhoneAuthProvider.ForceResendingToken) -> Unit,
    )

    fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        onSignUpSuccess: (userUid: String) -> Unit,
        onSignInSuccess: (userUid: String) -> Unit,
        onSignInFailed: () -> Unit,
    )

    suspend fun logIn(currentUserId: String)

    fun logOut()

    suspend fun deleteAccount(userUid: String)

    suspend fun upsertProfile(
        model: UserProfileModel,
        avatarUri: Uri? = null,
        portfolioUris: List<Uri>? = null,
        isNewUser: Boolean = false,
    )

    suspend fun getFeed(
        feedFilter: FeedFilter?
    ): List<UserProfileModel> // by filter

    suspend fun postReview(targetUserId: String, review: Review)

    suspend fun addFavourite(userId: String, folderName: String)

    suspend fun removeFavourite(userId: String, folderName: String)

    suspend fun getFavourites(): List<FavouriteFolder>

    suspend fun sendFeedback(userUid: String?, text: String)
    suspend fun getProfileById(userUid: String): UserProfileModel

    suspend fun getProfilesByIds(userIds: List<String>): List<UserProfileModel>
}