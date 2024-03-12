package ru.arinae_va.lensa.presentation.feature.auth.viewmodel

internal enum class OtpScreenInputField {
    OTP_CODE,
}
internal data class OtpScreenState(
    val phoneNumber: String,
    val otpInput: String,
    val isResendEnabled: Boolean,
    val isButtonNextEnabled: Boolean,
    val validationErrors: Map<OtpScreenInputField, String>,
)