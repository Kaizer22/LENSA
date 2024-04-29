package ru.arinae_va.lensa.presentation.feature.auth.viewmodel

import android.content.Context
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.presentation.common.StateViewModel
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import ru.arinae_va.lensa.utils.Constants
import ru.arinae_va.lensa.utils.isValidPhoneNumber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val navHostController: NavHostController,
) : StateViewModel<AuthScreenState>(
    initialState = AuthScreenState(
        isLoading = false,
        isEnabledNextButton = false,
        phoneNumber = Constants.RUSSIA_COUNTRY_CODE,
    )
) {


    // TODO не отображать ПРОДОЛЖИТЬ до ввода кода
    fun onPhoneNumberChanged(phoneNumber: String) {
        val errors = if (isValidPhoneNumber(phoneNumber)) {
            emptyMap()
        } else {
            mapOf(
                AuthScreenField.PHONE_NUMBER to
                        context.getString(R.string.invalid_phone_number_error)
            )
        }
        update(
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

    fun onBackPressed() {
        navHostController.popBackStack()
    }
}