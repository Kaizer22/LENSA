package ru.arinae_va.lensa.data.repositroy

import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import ru.arinae_va.lensa.data.datasource.remote.IUserProfileDataSource
import ru.arinae_va.lensa.domain.repository.IAuthRepository
import ru.arinae_va.lensa.domain.repository.IPresenceRepository
import ru.arinae_va.lensa.domain.repository.IUserProfileRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

sealed class AuthStatus {
    data class SignInSuccess(val userUid: String) : AuthStatus()
    data class SignUpSuccess(val userUid: String) : AuthStatus()
    data class Fail(val error: String) : AuthStatus()
}

sealed class PhoneVerificationStatus {

    data class SignInCompleted(val userId: String): PhoneVerificationStatus()
    data class SignUpCompleted(val userId: String): PhoneVerificationStatus()
    data class VerificationCompleted(val credentials: PhoneAuthCredential): PhoneVerificationStatus()

    data class VerificationFailed(val error: String) : PhoneVerificationStatus()

    data class CodeSent(
        val verificationId: String,
        val token: PhoneAuthProvider.ForceResendingToken,
    ): PhoneVerificationStatus()
}

class AuthRepository @Inject constructor(
    private val userProfileRepository: IUserProfileRepository,
    private val userInfoDataSource: IUserProfileDataSource,
    private val presenceRepository: IPresenceRepository,
) : IAuthRepository {
    override fun currentUserId(): String? = Firebase.auth.currentUser?.uid

    override suspend fun signInWithPhone(credentials: PhoneAuthCredential):
    PhoneVerificationStatus {
        val authStatus = signInWithPhoneAuthCredential(
            credential = credentials,
        )
        return when (authStatus) {
            is AuthStatus.SignInSuccess -> PhoneVerificationStatus.SignInCompleted(authStatus.userUid)

            is AuthStatus.SignUpSuccess -> PhoneVerificationStatus.SignUpCompleted(authStatus.userUid)

            is AuthStatus.Fail -> PhoneVerificationStatus.VerificationFailed(authStatus.error)
        }
    }


    override suspend fun verifyPhoneNumber(
        phoneNumber: String,
        //onSignInCompleted: (userUid: String) -> Unit,
        //onSignUpCompleted: (userUid: String) -> Unit,
        //onVerificationFailed: (String) -> Unit,
        //onCodeSent: (String, PhoneAuthProvider.ForceResendingToken) -> Unit,
    ): PhoneVerificationStatus = suspendCoroutine { cont ->
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                cont.resume(
                    PhoneVerificationStatus.VerificationCompleted(credential)
                )
            }

            override fun onVerificationFailed(e: FirebaseException) {
                cont.resume(PhoneVerificationStatus.VerificationFailed(e.message.orEmpty()))
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                cont.resume(
                    PhoneVerificationStatus.CodeSent(
                        verificationId = verificationId,
                        token = token,
                    )
                )
            }
        }

        val options = PhoneAuthOptions
            .newBuilder().setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(callbacks).build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override suspend fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        //onSignUpSuccess: (userUid: String) -> Unit,
        //onSignInSuccess: (userUid: String) -> Unit,
        //onSignInFailed: () -> Unit,
    ): AuthStatus {
        val authResult = Firebase.auth.signInWithCredential(credential).await()
        authResult.user?.uid?.let { userUid ->
            if (userInfoDataSource.isNewUser(userUid)) {
                return AuthStatus.SignUpSuccess(userUid)//onSignUpSuccess(userUid)
            } else {
                return AuthStatus.SignInSuccess(userUid)//onSignInSuccess(userUid)
            }
        } ?: run { return AuthStatus.Fail("") }
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    task.result?.user?.uid?.let { userUid ->
//                        GlobalScope.launch {
//                            if (userInfoDataSource.isNewUser(userUid)) {
//                                onSignUpSuccess(userUid)
//                            } else {
//                                onSignInSuccess(userUid)
//                            }
//                        }.wait()
//                    } ?: run { onSignInFailed() }
//                } else {
//                    onSignInFailed()
//                }
//            }
//            .addOnSuccessListener { result ->
//                result.user?.uid?.let { userUid ->
//                    GlobalScope.launch {
//                        if (userInfoDataSource.isNewUser(userUid)) {
//                            onSignUpSuccess(userUid)
//                        } else {
//                            onSignInSuccess(userUid)
//                        }
//                    }
//                } ?: run { onSignInFailed() }
//            }.addOnFailureListener {
//                onSignInFailed()
//            }.addOnCanceledListener {
//                onSignInFailed()
//            }
    }

    private val coroutineContext = Dispatchers.IO + Job()
    private val coroutineScope = CoroutineScope(coroutineContext)
    override fun logOut(
        isFullLogOut: Boolean,
    ) {
        coroutineScope.launch {
            userProfileRepository.currentProfileId()?.let {
                presenceRepository.setOffline()
            }
            userProfileRepository.clearCurrentUser()
            // TODO clear cache and database
            if (isFullLogOut) {
                Firebase.auth.signOut()
            }
        }
    }

    override suspend fun logIn(currentProfileId: String) {
        userProfileRepository.setCurrentUser(currentProfileId)
    }
}