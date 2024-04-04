package ru.arinae_va.lensa.data.repositroy

import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import ru.arinae_va.lensa.data.datasource.remote.IUserProfileDataSource
import ru.arinae_va.lensa.domain.repository.IAuthRepository
import ru.arinae_va.lensa.domain.repository.IUserProfileRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val userProfileRepository: IUserProfileRepository,
    private val userInfoDataSource: IUserProfileDataSource,
): IAuthRepository {
    override fun currentUserId(): String? = Firebase.auth.currentUser?.uid

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
                object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
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
                        userInfoDataSource.checkUid(
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
                    userInfoDataSource.checkUid(
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
        userProfileRepository.clearCurrentUser()
        // TODO clear cache and database
        Firebase.auth.signOut()
    }

    override suspend fun logIn(currentProfileId: String) {
        userProfileRepository.setCurrentUser(currentProfileId)
    }
}