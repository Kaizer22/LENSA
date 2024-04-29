package ru.arinae_va.lensa.presentation.feature.auth.viewmodel

enum class AuthScreenField {
    PHONE_NUMBER,
}
data class AuthScreenState(
    val phoneNumber: String,
    val isLoading: Boolean,
    val isEnabledNextButton: Boolean,
    val validationErrors: Map<AuthScreenField, String> = emptyMap(),
)