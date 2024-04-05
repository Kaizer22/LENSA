package ru.arinae_va.lensa.presentation.feature.auth.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.data.repositroy.AuthStatus
import ru.arinae_va.lensa.data.repositroy.PhoneVerificationStatus
import ru.arinae_va.lensa.domain.repository.IAuthRepository
import ru.arinae_va.lensa.domain.repository.ISettingsRepository
import ru.arinae_va.lensa.domain.repository.IUserProfileRepository
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import javax.inject.Inject

// todo move to constants
internal const val OTP_CODE_LENGTH = 6

@HiltViewModel
class OtpViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val navHostController: NavHostController,
    private val authRepository: IAuthRepository,
    private val userProfileRepository: IUserProfileRepository,
    private val settingsRepository: ISettingsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(
        OtpScreenState(
            phoneNumber = "",
            otpInput = "",
            isLoading = false,
            isOtpCodeResent = false,
            isButtonNextEnabled = false,
            isShowSelectProfileDialog = false,
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

    private fun setLoading(isLoading: Boolean) {
        _state.tryEmit(
            state.value.copy(
                isLoading = isLoading,
            )
        )
    }

    // TODO загрузка пока отправляется код
    private fun verifyPhoneNumber(phoneNumber: String) {
        viewModelScope.launch {
            setLoading(true)
            val result = authRepository.verifyPhoneNumber(
                phoneNumber = phoneNumber,
            )
            when (result) {
                is PhoneVerificationStatus.VerificationCompleted -> {
                    processPhoneCredentials(result.credentials)
                }
                is PhoneVerificationStatus.VerificationFailed -> {
                    navHostController.navigate(
                        route = "${LensaScreens.COMMON_ERROR_SCREEN.name}/" +
                                context.getString(R.string.code_sending_error)
                    )
                    Toast.makeText(context, result.error, Toast.LENGTH_LONG).show()
                    setLoading(false)
                }
                is PhoneVerificationStatus.CodeSent -> {
                    setLoading(false)
                    _state.tryEmit(
                        state.value.copy(
                            verificationId = result.verificationId,
                            token = result.token,
                            isButtonNextEnabled = state.value.validationErrors.isEmpty()
                        )
                    )
                }
                is PhoneVerificationStatus.SignInCompleted -> {
                    onAddProfile()
                }

                is PhoneVerificationStatus.SignUpCompleted -> {
                    processUserProfiles(result.userId)
                }
            }
//                onCodeSent = { vId, t ->
//                    setLoading(false)
//                    _state.tryEmit(
//                        state.value.copy(
//                            verificationId = vId,
//                            token = t,
//                            isButtonNextEnabled = state.value.validationErrors.isEmpty()
//                        )
//                    )
//                },
//                onSignInCompleted = {
//                    onAddProfile()
//                },
//                onSignUpCompleted = { currentUserId ->
//                    // TODO refactor
//                    viewModelScope.launch {
//                        processUserProfiles(currentUserId)
//                    }
//                },
//                onVerificationFailed = {
//                    navHostController.navigate(
//                        route = "${LensaScreens.COMMON_ERROR_SCREEN.name}/" +
//                                context.getString(R.string.code_sending_error)
//                    )
//                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
//                    setLoading(false)
//                },
        }
    }

    private suspend fun processUserProfiles(currentUserId: String) {
        val userProfiles = userProfileRepository.getProfilesByUserId(currentUserId)
        when {
            userProfiles.size == 1 -> {
                with(userProfiles.first()) { onSelectProfile(profileId) }
            }

            userProfiles.size > 1 -> {
                _state.tryEmit(
                    state.value.copy(
                        isShowSelectProfileDialog = true,
                    )
                )
            }
        }
    }

    fun onAddProfile() {
        setLoading(false)
        navHostController.navigate(LensaScreens.REGISTRATION_ROLE_SELECTOR_SCREEN.name)
    }

    fun onSelectProfile(profileId: String) {
        viewModelScope.launch {
            authRepository.logIn(profileId)
            settingsRepository.updateLastLoggedInUser(profileId)
            navHostController.navigate(LensaScreens.FEED_SCREEN.name)
            setLoading(false)
        }
    }

    private suspend fun processPhoneCredentials(credential: PhoneAuthCredential) {
        when (val result = authRepository.signInWithPhoneAuthCredential(credential)) {
            is AuthStatus.SignInSuccess -> { processUserProfiles(result.userUid) }
            is AuthStatus.Fail -> {
                setLoading(false)
                _state.tryEmit(
                    state.value.copy(
                        validationErrors = mapOf(
                            OtpScreenInputField.OTP_CODE
                                    to context.getString(R.string.wrong_otp_code_error)
                        )
                    )
                )
            }
            is AuthStatus.SignUpSuccess -> {
                navHostController.navigate(LensaScreens.REGISTRATION_ROLE_SELECTOR_SCREEN.name)
                setLoading(false)
            }
        }
    }

    fun verifyCode() {
        val code = state.value.otpInput
        viewModelScope.launch {
            setLoading(true)
            state.value.verificationId?.let { vId ->
                val credential = PhoneAuthProvider.getCredential(vId, code)
                processPhoneCredentials(credential)
//                    onSignInFailed = {
//                        setLoading(false)
//                        _state.tryEmit(
//                            state.value.copy(
//                                validationErrors = mapOf(
//                                    OtpScreenInputField.OTP_CODE
//                                            to context.getString(R.string.wrong_otp_code_error)
//                                )
//                            )
//                        )
//                    },
//                    onSignUpSuccess = {
//                        navHostController.navigate(LensaScreens.REGISTRATION_ROLE_SELECTOR_SCREEN.name)
//                        setLoading(false)
//                    },
//                    onSignInSuccess = { currentUserId ->
//                        viewModelScope.launch {
//                            processUserProfiles(currentUserId)
//                        }
//                    }
//                )
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