package ru.arinae_va.lensa.domain.repository

import android.net.Uri
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import ru.arinae_va.lensa.domain.model.SpecialistModel

interface IUserInfoRepository {
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

    fun logOut()

    suspend fun deleteAccount(userUid: String)

    suspend fun upsertProfile(
        model: SpecialistModel,
        avatarUri: Uri? = null,
        portfolioUris: List<Uri>? = null,
        isNewUser: Boolean = false,
    )

    suspend fun getFeed(): List<SpecialistModel> // by filter

    fun postReview()

    fun addFavourite()

    fun removeFavourite()

    suspend fun sendFeedback(userUid: String?, text: String)
}