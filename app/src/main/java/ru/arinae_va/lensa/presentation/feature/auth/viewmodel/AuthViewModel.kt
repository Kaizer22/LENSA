package ru.arinae_va.lensa.presentation.feature.auth.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import javax.inject.Inject

private val RUSSIA_COUNTRY_CODE = "+7"
private val RUSSIAN_PHONE_NUMBER_LENGTH = 12

@HiltViewModel
class AuthViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val navHostController: NavHostController,
) : ViewModel() {

    private val _state = MutableStateFlow(
        AuthScreenState(
            isEnabledNextButton = false,
            phoneNumber = RUSSIA_COUNTRY_CODE,
        )
    )
    internal val state: StateFlow<AuthScreenState> = _state

    fun onPhoneNumberChanged(phoneNumber: String) {
        val errors = if (isValidPhoneNumber(phoneNumber)) {
            emptyMap()
        } else {
            mapOf(
                AuthScreenField.PHONE_NUMBER to
                        context.getString(R.string.invalid_phone_number_error)
            )
        }
        _state.tryEmit(
            state.value.copy(
                phoneNumber = phoneNumber,
                validationErrors = errors,
                isEnabledNextButton = errors.isEmpty()
            )
        )
    }

    fun onEnterPhoneNumber() {
        navHostController.navigate("${LensaScreens.OTP_SCREEN.name}/${state.value.phoneNumber}")
    }

    private fun isValidPhoneNumber(phoneNumber: String) = phoneNumber
        .startsWith(RUSSIA_COUNTRY_CODE) && phoneNumber.length == RUSSIAN_PHONE_NUMBER_LENGTH

    fun onBackPressed() {
        navHostController.popBackStack()
    }
}