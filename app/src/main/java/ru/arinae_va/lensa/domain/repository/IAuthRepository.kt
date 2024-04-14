package ru.arinae_va.lensa.domain.repository

import com.google.firebase.auth.PhoneAuthCredential
import ru.arinae_va.lensa.data.repositroy.AuthStatus
import ru.arinae_va.lensa.data.repositroy.PhoneVerificationStatus

// TODO make a manager nor repo?
interface IAuthRepository {
    fun currentUserId(): String?

    suspend fun verifyPhoneNumber(
        phoneNumber: String,
    ): PhoneVerificationStatus

    suspend fun signInWithPhone(credentials: PhoneAuthCredential): PhoneVerificationStatus

    suspend fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
    ): AuthStatus

    suspend fun logIn(currentProfileId: String)

    fun logOut(
        isFullLogOut: Boolean = true,
    )
}