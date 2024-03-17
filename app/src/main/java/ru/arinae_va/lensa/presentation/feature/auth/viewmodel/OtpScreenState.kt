package ru.arinae_va.lensa.presentation.feature.auth.viewmodel

import com.google.firebase.auth.PhoneAuthProvider

internal enum class OtpScreenInputField {
    OTP_CODE,
}
internal data class OtpScreenState(
    val phoneNumber: String,
    val verificationId: String?,
    val token: PhoneAuthProvider.ForceResendingToken?,
    val otpInput: String,
    val isOtpCodeResent: Boolean,
    val isResendEnabled: Boolean,
    val isButtonNextEnabled: Boolean,
    val validationErrors: Map<OtpScreenInputField, String>,
)