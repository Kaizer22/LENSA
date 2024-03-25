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

    private val _state = MutableStateFlow(
        OtpScreenState(
            phoneNumber = "",
            otpInput = "",
            isOtpCodeResent = false,
            isButtonNextEnabled = false,
            isResendEnabled = false,
            validationErrors = emptyMap(),
            verificationId = null,
            token = null,
        )
    )
    internal val state: StateFlow<OtpScreenState> = _state

    fun onAttach(phoneNumber: String) {
        if (!state.value.phoneNumberVerified) {
            verifyPhoneNumber(phoneNumber)
            _state.tryEmit(
                state.value.copy(
                    phoneNumber = phoneNumber,
                    phoneNumberVerified = true
                )
            )
        }
    }

    fun onOtpInputChanged(otpInput: String) {
        val errors = if (isValidOtp(otpInput)) emptyMap()
        else {
            mapOf(
                OtpScreenInputField.OTP_CODE to context.getString(R.string.otp_validation_error)
            )
        }
        _state.tryEmit(
            state.value.copy(
                otpInput = otpInput,
                validationErrors = errors,
                isButtonNextEnabled = errors.isEmpty() && state.value.verificationId != null,
            )
        )
    }

    private fun isValidOtp(otpInput: String) = otpInput.isDigitsOnly() &&
            otpInput.length == OTP_CODE_LENGTH

    // TODO загрузка пока отправляется код
    private fun verifyPhoneNumber(phoneNumber: String) {
        viewModelScope.launch {
            userInfoRepository.verifyPhoneNumber(
                phoneNumber = phoneNumber,
                onCodeSent = { vId, t ->
                    _state.tryEmit(
                        state.value.copy(
                            verificationId = vId,
                            token = t,
                            isButtonNextEnabled = state.value.validationErrors.isEmpty()
                        )
                    )
                },
                onSignInCompleted = {
                    navHostController.navigate(LensaScreens.REGISTRATION_ROLE_SELECTOR_SCREEN.name)
                },
                onSignUpCompleted = { currentUserId ->
                    // TODO refactor
                    viewModelScope.launch {
                        userInfoRepository.logIn(currentUserId)
                        navHostController.navigate(LensaScreens.FEED_SCREEN.name)
                    }
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
            state.value.verificationId?.let { vId ->
                val credential = PhoneAuthProvider.getCredential(vId, code)
                userInfoRepository.signInWithPhoneAuthCredential(
                    credential,
                    onSignInFailed = {
                        _state.tryEmit(
                            state.value.copy(
                                validationErrors = mapOf(
                                    OtpScreenInputField.OTP_CODE
                                            to context.getString(R.string.wrong_otp_code_error)
                                )
                            )
                        )
                    },
                    onSignUpSuccess = {
                        navHostController.navigate(LensaScreens.REGISTRATION_ROLE_SELECTOR_SCREEN.name)
                    },
                    onSignInSuccess = { currentUserId ->
                        viewModelScope.launch {
                            userInfoRepository.logIn(currentUserId)
                            navHostController.navigate(LensaScreens.FEED_SCREEN.name)
                        }
                    }
                )
            }
        }
    }


    fun onResendOtp() {
        _state.tryEmit(
            state.value.copy(
                isOtpCodeResent = true,
                isResendEnabled = false,
            )
        )
    }

    fun enableResend() {
        _state.tryEmit(
            state.value.copy(
                isResendEnabled = true,
            )
        )
    }
}