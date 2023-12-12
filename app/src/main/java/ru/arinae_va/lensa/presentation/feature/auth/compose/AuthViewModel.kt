package ru.arinae_va.lensa.presentation.feature.auth.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.domain.repository.IUserInfoRepository
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val navHostController: NavHostController,
    private val userInfoRepository: IUserInfoRepository,
) : ViewModel() {

    private var verificationId: String? = null
    private var token: PhoneAuthProvider.ForceResendingToken? = null
    fun onEnterPhone(phoneNumber: String) {
        //userInfoRepository.verifyPhoneNumber(phoneNumber)
        navHostController.navigate("${LensaScreens.OTP_SCREEN.name}/$phoneNumber")
    }

    fun verifyPhoneNumber(phoneNumber: String) {
        viewModelScope.launch {
            userInfoRepository.verifyPhoneNumber(
                phoneNumber = phoneNumber,
                onCodeSent = { vId, t ->
                    verificationId = vId
                    token = t
                },
                onSignInCompleted  = {
                    navHostController.navigate(LensaScreens.REGISTRATION_ROLE_SELECTOR_SCREEN.name)
                },
                onSignUpCompleted = {
                    navHostController.navigate(LensaScreens.FEED_SCREEN.name)
                },
                onVerificationFailed = {
                    navHostController.navigate(
                        route = "${LensaScreens.COMMON_ERROR_SCREEN.name}/$it"
                    )
                },
            )
        }
    }

    fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        userInfoRepository.signInWithPhoneAuthCredential(
            credential,
            onSignInFailed = {
                navHostController.navigate(
                    route = "${LensaScreens.COMMON_ERROR_SCREEN.name}/{ОШИБКА2}"
                )
            },
            onSignUpSuccess = {
                navHostController.navigate(LensaScreens.REGISTRATION_ROLE_SELECTOR_SCREEN.name)
            },
            onSignInSuccess = { userId ->
                navHostController.navigate(LensaScreens.FEED_SCREEN.name)
            }
        )
    }

    fun onResendOtp() {

    }

    fun onBackPressed() {
        navHostController.popBackStack()
    }
}