package ru.arinae_va.lensa.presentation.feature.auth.viewmodel

import com.google.firebase.auth.PhoneAuthProvider
import ru.arinae_va.lensa.domain.model.UserProfileModel

internal enum class OtpScreenInputField {
    OTP_CODE,
}
internal data class OtpScreenState(
    val userProfiles: List<UserProfileModel>,
    val phoneNumber: String,
    val phoneNumberVerified: Boolean = false,
    val verificationId: String?,
    val token: PhoneAuthProvider.ForceResendingToken?,
    val otpInput: String,
    val isShowSelectProfileDialog: Boolean,
    val isLoading: Boolean,
    val isOtpCodeResent: Boolean,
    val isResendEnabled: Boolean,
    val isButtonNextEnabled: Boolean,
    val validationErrors: Map<OtpScreenInputField, String>,
)