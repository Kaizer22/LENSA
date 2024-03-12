package ru.arinae_va.lensa.presentation.feature.auth.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.domain.repository.IUserInfoRepository
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import javax.inject.Inject

// todo move to constants
internal const val OTP_CODE_LENGTH = 6
@HiltViewModel
class OtpViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val navHostController: NavHostController,
    private val userInfoRepository: IUserInfoRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(OtpScreenState(
        phoneNumber = "",
        otpInput = "",
        isButtonNextEnabled = false,
        isResendEnabled = false,
        validationErrors = emptyMap(),
    ))
    internal val state: StateFlow<OtpScreenState> = _state

    private var verificationId: String? = null
    private var token: PhoneAuthProvider.ForceResendingToken? = null

    fun onAttach(phoneNumber: String) {
        verifyPhoneNumber(phoneNumber)
        _state.tryEmit(
            state.value.copy(
                phoneNumber = phoneNumber,
            )
        )
    }

    fun onOtpInputChanged(otpInput: String) {
        val errors = if(isValidOtp(otpInput)) emptyMap()
        else {
            mapOf(
                OtpScreenInputField.OTP_CODE to context.getString(R.string.otp_validation_error)
            )
        }
        _state.tryEmit(
            state.value.copy(
                validationErrors = errors,
                isButtonNextEnabled = errors.isEmpty(),
            )
        )
    }

    private fun isValidOtp(otpInput: String) = otpInput.isDigitsOnly() &&
            otpInput.length == OTP_CODE_LENGTH

    private fun verifyPhoneNumber(phoneNumber: String) {
        viewModelScope.launch {
            userInfoRepository.verifyPhoneNumber(
                phoneNumber = phoneNumber,
                onCodeSent = { vId, t ->
                    verificationId = vId
                    token = t
                },
                onSignInCompleted = {
                    navHostController.navigate(LensaScreens.REGISTRATION_ROLE_SELECTOR_SCREEN.name)
                },
                onSignUpCompleted = {
                    navHostController.navigate(LensaScreens.FEED_SCREEN.name)
                },
                onVerificationFailed = {
                    navHostController.navigate(
                        route = "${LensaScreens.COMMON_ERROR_SCREEN.name}/" +
                                context.getString(R.string.code_sending_error)
                    )
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                },
            )
        }
    }

    fun verifyCode() {
        val code = state.value.otpInput
        viewModelScope.launch {
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
    }
    fun onResendOtp() {

    }
}