package ru.arinae_va.lensa.presentation.feature.registration.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.domain.model.SocialMedia
import ru.arinae_va.lensa.domain.model.SpecialistModel
import ru.arinae_va.lensa.domain.repository.IUserInfoRepository
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val navHostController: NavHostController,
    private val userInfoRepository: IUserInfoRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(
        RegistrationScreenState.INITIAL
    )
    internal val state: StateFlow<RegistrationScreenState> = _state

    fun setType(isSpecialistScreenSelected: Boolean) {
        _state.tryEmit(
            state.value.copy(isSpecialistRegistrationScreen = isSpecialistScreenSelected)
        )
    }

    fun onNameChanged(name: String) {
        _state.tryEmit(
            state.value.copy(name = name)
        )
    }

    fun onSurnameChanged(surname: String) {

    }

    fun onSpecializationChanged(specialization: String) {

    }

    fun onCountryChanged(country: String) {

    }

    fun onCityChanged(city: String) {

    }

    fun onPhoneNumberChanged(phoneNumber: String) {

    }

    fun onEmailChanged(email: String) {

    }

    fun onAboutChanged(about: String) {

    }

    fun onPersonalWebsiteChanged(personalWebsite: String) {

    }

    fun onGetInTouchClick() {
        navHostController.navigate(LensaScreens.FEEDBACK_SCREEN.name)
    }

    // TODO отображение загрузки
    fun onSaveClick() {
        if (validateFields()) {
            viewModelScope.launch {
                with(state.value) {
                    val model = SpecialistModel(
                        id = Firebase.auth.currentUser?.uid ?: "",
                        name = name,
                        surname = surname,
                        specialization = specialization,
                        country = country,
                        city = city,
                        personalSite = personalSite,
                        email = email,
                        about = about,
                        socialMedias = socialMedias.map {
                            SocialMedia(
                                link = it.value,
                                type = it.key,
                            )
                        },
                        prices = prices,
                    )
                    userInfoRepository.upsertProfile(
                        model = model,
                        avatarUri = avatarUri,
                        portfolioUris = portfolioUris,
                        isNewUser = true,
                    )
                }
                navHostController.navigate(LensaScreens.FEED_SCREEN.name)
            }
        }
    }

    private fun validateFields(): Boolean {
        with(state.value) {
            val validationErrors = mutableMapOf<RegistrationScreenInputField, String>()
            if (name.isBlank())
                validationErrors[RegistrationScreenInputField.NAME] =
                    context.getString(R.string.registration_screen_name_validation_error)
            if (surname.isBlank())
                validationErrors[RegistrationScreenInputField.SURNAME] =
                    context.getString(R.string.registration_screen_surname_validation_error)
            if (country.isBlank())
                validationErrors[RegistrationScreenInputField.COUNTRY] =
                    context.getString(R.string.registration_screen_country_validation_error)
            if (city.isBlank())
                validationErrors[RegistrationScreenInputField.CITY] =
                    context.getString(R.string.registration_screen_city_validation_error)
            if (specialization.isNotBlank())
                validationErrors[RegistrationScreenInputField.SPECIALIZATION] =
                    context.getString(R.string.registration_screen_specialization_validation_error)
            if (prices.isNotEmpty())
                validationErrors[RegistrationScreenInputField.PRICES] =
                    context.getString(R.string.registration_screen_prices_validation_error)
            if (avatarUri != null)
                validationErrors[RegistrationScreenInputField.AVATAR] =
                    context.getString(R.string.registration_screen_avatar_validation_error)

            _state.tryEmit(
                state.value.copy(
                    validationErrors = validationErrors,
                )
            )
            return validationErrors.isEmpty()
        }
    }

    fun onSelectAccountTypeClick(isSpecialist: Boolean) {
        navHostController.navigate("${LensaScreens.REGISTRATION_SCREEN.name}/$isSpecialist")
    }
}