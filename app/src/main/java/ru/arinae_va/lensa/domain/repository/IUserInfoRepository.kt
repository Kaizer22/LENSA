package ru.arinae_va.lensa.domain.repository

import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

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

    fun deleteAccount()

    fun trySignInUser()

    fun editProfile()

    fun getFeed() // by filter

    fun postReview()

    fun addFavourite()

    fun removeFavourite()

    fun sendFeedBack()

}