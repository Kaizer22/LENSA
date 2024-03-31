package ru.arinae_va.lensa.data.repositroy

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.auth.auth
import ru.arinae_va.lensa.data.datasource.local.IFavouritesStorage
import ru.arinae_va.lensa.data.datasource.remote.IUserInfoDataSource
import ru.arinae_va.lensa.data.model.FavouriteFolder
import ru.arinae_va.lensa.domain.model.FeedFilter
import ru.arinae_va.lensa.domain.model.Review
import ru.arinae_va.lensa.domain.model.UserProfileModel
import ru.arinae_va.lensa.domain.repository.IUserInfoRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

// TODO разделить на репозитории по функционалу
class UserInfoRepository @Inject constructor(
    private val userInfoStorage: IUserInfoDataSource,
    private val favouritesStorage: IFavouritesStorage,
    //private val settingsStorage: ISettingsStorage,
) : IUserInfoRepository {

    private var currentUserProfile: UserProfileModel? = null

    override fun currentUserId(): String? = currentUserProfile?.id
        ?: Firebase.auth.currentUser?.uid

    override fun currentUserProfile(): UserProfileModel? = currentUserProfile

    override fun verifyPhoneNumber(
        phoneNumber: String,
        onSignInCompleted: (userUid: String) -> Unit,
        onSignUpCompleted: (userUid: String) -> Unit,
        onVerificationFailed: (String) -> Unit,
        onCodeSent: (String, PhoneAuthProvider.ForceResendingToken) -> Unit,
    ) {
        val options = PhoneAuthOptions
            .newBuilder().setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(
                object : OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        signInWithPhoneAuthCredential(
                            credential = credential,
                            onSignUpSuccess = { uid ->
                                onSignUpCompleted(uid)
                            },
                            onSignInSuccess = { uid ->
                                onSignInCompleted(uid)
                            },
                            onSignInFailed = {
                                onVerificationFailed("Sign in fail")
                            },
                        )
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        onVerificationFailed(e.message ?: "")
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        onCodeSent(verificationId, token)
                    }
                }
            ).build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        onSignUpSuccess: (userUid: String) -> Unit,
        onSignInSuccess: (userUid: String) -> Unit,
        onSignInFailed: () -> Unit,
    ) {
        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userUid = task.result?.user?.uid
                    if (userUid != null) {
                        userInfoStorage.checkUid(
                            userUid,
                            onCheckResult = { isNewUser ->
                                if (isNewUser) {
                                    onSignUpSuccess(userUid)
                                } else {
                                    onSignInSuccess(userUid)
                                }
                            }
                        )
                    } else {
                        onSignInFailed()
                    }
                } else {
                    onSignInFailed()
                }
            }
            .addOnSuccessListener { result ->
                val userUid = result.user?.uid
                if (userUid != null) {
                    userInfoStorage.checkUid(
                        userUid,
                        onCheckResult = { isNewUser ->
                            if (isNewUser) {
                                onSignUpSuccess(userUid)
                            } else {
                                onSignInSuccess(userUid)
                            }
                        }
                    )
                } else {
                    onSignInFailed()
                }
            }.addOnFailureListener {
                onSignInFailed()
            }.addOnCanceledListener {
                onSignInFailed()
            }
    }

    override fun logOut() {
        //settingsStorage.lastLoggedInUser = ""
        currentUserProfile = null
        // TODO clear cache and database
        Firebase.auth.signOut()
    }

    override suspend fun logIn(currentUserId: String) {
        currentUserProfile = getProfileById(currentUserId)
        //settingsStorage.lastLoggedInUser = currentUserId
    }

    override suspend fun deleteAccount() {
        currentUserProfile?.let {
            userInfoStorage.deleteAccount(it.id)
            //settingsStorage.lastLoggedInUser = null
        }
    }

    override suspend fun upsertProfile(
        model: UserProfileModel,
        avatarUri: Uri?,
        portfolioUris: List<Uri>?,
        isNewUser: Boolean,
    ) {
        if (isNewUser) {
            userInfoStorage.createProfile(profile = model)
        } else {
            userInfoStorage.updateProfile(profile = model)
        }
        avatarUri?.let {
            userInfoStorage.uploadAvatarImage(model.id, it)
        }
        portfolioUris?.let {
            it.forEach { imageUri ->
                userInfoStorage.uploadPortfolioImage(model.id, imageUri)
            }
        }
    }

    // TODO caching
    override suspend fun getFeed(feedFilter: FeedFilter?): List<UserProfileModel> {
        currentUserProfile?.let {
            return userInfoStorage.getFeed(it.id, feedFilter)
        }
        return emptyList()
    }


    override suspend fun postReview(targetUserId: String, review: Review) {
        userInfoStorage.postReview(targetUserId, review)
    }

    override suspend fun addFavourite(userId: String, folderName: String) {
        currentUserProfile?.let {
            favouritesStorage.addFavourite(
                hostUserId = it.id,
                userId = userId,
                folderName = folderName,
            )
        }
    }

    override suspend fun removeFavourite(userId: String, folderName: String) {
        currentUserProfile?.let {
            favouritesStorage.removeFavourite(
                hostUserId = it.id,
                userId = userId,
                folderName = folderName,
            )
        }
    }


    override suspend fun getFavourites(): List<FavouriteFolder> = currentUserProfile?.let {
        favouritesStorage.getFoldersByHostId(it.id)
    } ?: emptyList()


    override suspend fun sendFeedback(userUid: String?, text: String) =
        userInfoStorage.sendFeedback(userUid, text)

    override suspend fun getProfileById(userUid: String): UserProfileModel =
        userInfoStorage.getProfileById(userUid)

    override suspend fun getProfilesByIds(userIds: List<String>): List<UserProfileModel> =
        userInfoStorage.getProfilesByIds(userIds)
}