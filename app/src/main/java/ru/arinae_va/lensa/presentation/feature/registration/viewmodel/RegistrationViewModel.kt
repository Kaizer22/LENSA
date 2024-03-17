package ru.arinae_va.lensa.presentation.feature.registration.viewmodel

import android.content.Context
import android.net.Uri
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
import ru.arinae_va.lensa.domain.model.Price
import ru.arinae_va.lensa.domain.model.SocialMedia
import ru.arinae_va.lensa.domain.model.UserProfileModel
import ru.arinae_va.lensa.domain.model.UserProfileType
import ru.arinae_va.lensa.domain.repository.IUserInfoRepository
import ru.arinae_va.lensa.presentation.feature.feed.compose.SocialMediaType
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import ru.arinae_va.lensa.utils.Constants
import ru.arinae_va.lensa.utils.isValidPhoneNumber
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
        _state.tryEmit(
            state.value.copy(surname = surname)
        )
    }

    fun onSpecializationChanged(specialization: String) {
        _state.tryEmit(
            state.value.copy(specialization = specialization)
        )
    }

    fun onCountryChanged(country: String) {
        _state.tryEmit(
            state.value.copy(country = country)
        )
    }

    fun onCityChanged(city: String) {
        _state.tryEmit(
            state.value.copy(city = city)
        )
    }

    fun onPhoneNumberChanged(phoneNumber: String) {
        val errors = if (isValidPhoneNumber(phoneNumber)) {
            emptyMap()
        } else {
            mapOf(
                RegistrationScreenInputField.PHONE_NUMBER to
                        context.getString(R.string.invalid_phone_number_error)
            )
        }
        _state.tryEmit(
            state.value.copy(
                phoneNumber = phoneNumber,
                validationErrors = errors,
            )
        )
    }

    fun onEmailChanged(email: String) {
        _state.tryEmit(
            state.value.copy(email = email)
        )
    }

    fun onAboutChanged(about: String) {
        _state.tryEmit(
            state.value.copy(about = about)
        )
    }

    fun onPersonalWebsiteChanged(personalWebsite: String) {
        _state.tryEmit(
            state.value.copy(personalSite = personalWebsite)
        )
    }

    fun onPricesChanged(prices: List<Price>) {
        _state.tryEmit(
            state.value.copy(prices = prices)
        )
    }

    fun onSocialMediasChanged(socialMedias: Map<SocialMediaType, String>) {
        _state.tryEmit(
            state.value.copy(socialMedias = socialMedias)
        )
    }

    fun onAvatarChanged(avatar: Uri) {
        _state.tryEmit(
            state.value.copy(avatarUri = avatar)
        )
    }

    fun onPortfolioChanged(portfolio: List<Uri>) {
        _state.tryEmit(
            state.value.copy(
                portfolioUris = portfolio,
            )
        )
    }

    fun onGetInTouchClick() {
        navHostController.navigate(LensaScreens.FEEDBACK_SCREEN.name)
    }

    // TODO отображение загрузки
    fun onSaveClick() {
        viewModelScope.launch {
            if (state.value.isSpecialistRegistrationScreen) {
                registerSpecialist()
            } else {
                registerCustomer()
            }
        }
    }

    private suspend fun registerCustomer() {
        if (validateCustomerFields()) {
            with(state.value) {
                val model = UserProfileModel(
                    id = Firebase.auth.currentUser?.uid ?: "",
                    type = UserProfileType.CUSTOMER,
                    name = name,
                    surname = surname,
                    specialization = Constants.CUSTOMER_SPECIALIZATION,
                    country = country,
                    city = city,
                    socialMedias = socialMedias.map {
                        SocialMedia(
                            link = it.value,
                            type = it.key,
                        )
                    },
                    about = about,
                    personalSite = personalSite,
                    email = email,
                )

                userInfoRepository.upsertProfile(
                    model = model,
                    avatarUri = avatarUri,
                    isNewUser = true,
                )
                navHostController.navigate(LensaScreens.FEED_SCREEN.name)
            }
        }
    }

    private suspend fun registerSpecialist() {
        if (validateSpecialistFields()) {
            with(state.value) {
                val model = UserProfileModel(
                    id = Firebase.auth.currentUser?.uid ?: "",
                    type = UserProfileType.SPECIALIST,
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
                navHostController.navigate(LensaScreens.FEED_SCREEN.name)
            }
        }
    }

    private fun validateCustomerFields(): Boolean {
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
            _state.tryEmit(
                state.value.copy(
                    validationErrors = validationErrors,
                )
            )
            return validationErrors.isEmpty()
        }
    }

    private fun validateSpecialistFields(): Boolean {
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
            if (specialization.isBlank())
                validationErrors[RegistrationScreenInputField.SPECIALIZATION] =
                    context.getString(R.string.registration_screen_specialization_validation_error)
            if (prices.isEmpty())
                validationErrors[RegistrationScreenInputField.PRICES] =
                    context.getString(R.string.registration_screen_prices_validation_error)
            if (avatarUri == null)
                validationErrors[RegistrationScreenInputField.AVATAR] =
                    context.getString(R.string.registration_screen_avatar_validation_error)
            if (portfolioUris.isEmpty())
                validationErrors[RegistrationScreenInputField.PORTFOLIO] =
                    context.getString(R.string.registration_screen_portfolio_validation_error)

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