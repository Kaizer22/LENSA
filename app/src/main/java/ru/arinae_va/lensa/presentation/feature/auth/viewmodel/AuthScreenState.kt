package ru.arinae_va.lensa.presentation.feature.auth.viewmodel

internal enum class AuthScreenField {
    PHONE_NUMBER,
}
internal data class AuthScreenState(
    val phoneNumber: String,
    val isEnabledNextButton: Boolean,
    val validationErrors: Map<AuthScreenField, String> = emptyMap(),
)