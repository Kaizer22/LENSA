package ru.arinae_va.lensa.data.repositroy

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.auth.auth
import ru.arinae_va.lensa.data.datasource.remote.IUserInfoStorage
import ru.arinae_va.lensa.domain.model.SpecialistModel
import ru.arinae_va.lensa.domain.repository.IUserInfoRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

// TODO разделить на репозитории по функционалу
class UserInfoRepository @Inject constructor(
    private val userInfoStorage: IUserInfoStorage,
) : IUserInfoRepository {
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
        Firebase.auth.signOut()
    }

    override suspend fun deleteAccount(userUid: String) {
        userInfoStorage.deleteAccount(userUid)
    }

    override suspend fun upsertProfile(
        model: SpecialistModel,
        avatarUri: Uri?,
        portfolioUris: List<Uri>?,
        isNewUser: Boolean,
    ) {
        if (isNewUser) {
            userInfoStorage.createProfile(profile = model)
        } else {
            userInfoStorage.updateProfile(model)
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

    override suspend fun getFeed(): List<SpecialistModel> {
        return userInfoStorage.getFeed()
    }

    override fun postReview() {
        TODO("Not yet implemented")
    }

    override fun addFavourite() {
        TODO("Not yet implemented")
    }

    override fun removeFavourite() {
        TODO("Not yet implemented")
    }

    override suspend fun sendFeedback(userUid: String?, text: String) {
        userInfoStorage.sendFeedback(userUid, text)
    }

    override suspend fun getProfileById(userUid: String): SpecialistModel {
        return userInfoStorage.getProfileById(userUid)
    }
}