package ru.arinae_va.lensa.presentation.feature.registration.viewmodel

import android.content.Context
import android.net.Uri
import android.webkit.URLUtil.isHttpsUrl
import android.webkit.URLUtil.isValidUrl
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.R
import ru.arinae_va.lensa.domain.model.user.Price
import ru.arinae_va.lensa.domain.model.user.SocialMedia
import ru.arinae_va.lensa.domain.model.user.UserProfileModel
import ru.arinae_va.lensa.domain.model.user.UserProfileType
import ru.arinae_va.lensa.domain.repository.IAuthRepository
import ru.arinae_va.lensa.domain.repository.IUserProfileRepository
import ru.arinae_va.lensa.presentation.common.StateViewModel
import ru.arinae_va.lensa.presentation.feature.feed.compose.SocialMediaType
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import ru.arinae_va.lensa.utils.Constants
import ru.arinae_va.lensa.utils.isValidEmail
import ru.arinae_va.lensa.utils.isValidPhoneNumber
import javax.inject.Inject

internal const val EMPTY_USER_ID = "empty"

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val navHostController: NavHostController,
    private val userProfileRepository: IUserProfileRepository,
    private val authRepository: IAuthRepository,
) : StateViewModel<RegistrationScreenState>(
    initialState = RegistrationScreenState.INITIAL
) {
    fun setUser(editUserId: String) {
        if (editUserId == userProfileRepository.currentProfileId()) {
            viewModelScope.launch {
                setLoading(true)
                val editableUser = userProfileRepository.getProfileById(editUserId)

                with(editableUser) {
                    val socialMediasMap = mutableMapOf<SocialMediaType, String>()
                    socialMedias.forEach { socialMediasMap[it.type] = it.link }
                    update(
                        state.value.copy(
                            isLoading = false,
                            isEdit = true,
                            name = name,
                            surname = surname,
                            specialization = specialization,
                            avatarUri = avatarUrl?.let { Uri.parse(avatarUrl) },
                            country = country,
                            city = city,
                            personalSite = personalSite,
                            email = email,
                            about = about,
                            socialMedias = socialMediasMap,
                            prices = prices,
                            portfolioUris = portfolioUrls?.map { Uri.parse(it) } ?: emptyList(),
                            rating = rating,
                            phoneNumber = phoneNumber,
                        )
                    )
                }
            }
        }
    }

    fun setLoading(isLoading: Boolean) {
        update(
            state.value.copy(isLoading = isLoading)
        )
    }

    fun setType(isSpecialistScreenSelected: Boolean) {
        update(
            state.value.copy(isSpecialistRegistrationScreen = isSpecialistScreenSelected)
        )
    }

    fun onNameChanged(name: String) {
        update(
            state.value.copy(name = name)
        )
    }

    fun onSurnameChanged(surname: String) {
        update(
            state.value.copy(surname = surname)
        )
    }

    fun onSpecializationChanged(specialization: String) {
        update(
            state.value.copy(specialization = specialization)
        )
    }

    fun onCountryChanged(country: String) {
        update(
            state.value.copy(country = country)
        )
    }

    fun onCityChanged(city: String) {
        update(
            state.value.copy(city = city)
        )
    }

    fun onPhoneNumberChanged(phoneNumber: String) {
        update(
            state.value.copy(
                phoneNumber = phoneNumber,
            )
        )
    }

    fun onEmailChanged(email: String) {
        update(
            state.value.copy(email = email)
        )
    }

    fun onAboutChanged(about: String) {
        update(
            state.value.copy(about = about)
        )
    }

    fun onPersonalWebsiteChanged(personalWebsite: String) {
        update(
            state.value.copy(personalSite = personalWebsite)
        )
    }

    fun onPricesChanged(prices: List<Price>) {
        update(
            state.value.copy(prices = prices)
        )
    }

    fun onSocialMediasChanged(socialMedias: Map<SocialMediaType, String>) {
        update(
            state.value.copy(socialMedias = socialMedias)
        )
    }

    fun onAvatarChanged(avatar: Uri) {
        update(
            state.value.copy(avatarUri = avatar)
        )
    }

    fun onPortfolioChanged(portfolio: List<Uri>) {
        update(
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
            setLoading(true)
            if (state.value.isSpecialistRegistrationScreen) {
                upsertSpecialist()
            } else {
                upsertCustomer()
            }
            setLoading(false)
        }
    }

    private suspend fun finishUpsert(newUserId: String?) {
        if (!state.value.isEdit) {
            authRepository.logIn(newUserId.orEmpty())
        }
        if (!state.value.isEdit) {
            navHostController.navigate(route = LensaScreens.FEED_SCREEN.name,
                navOptions = NavOptions.Builder()
                    .setPopUpTo(LensaScreens.FEED_SCREEN.name, inclusive = true)
                    .build()
            )
        } else {
            navHostController.navigate(LensaScreens.FEED_SCREEN.name)
        }
    }

    private suspend fun upsertCustomer() {
        if (validateCustomerFields()) {
            with(state.value) {
                val model = UserProfileModel(
                    userId = authRepository.currentUserId().orEmpty(),
                    profileId = userProfileRepository.currentProfileId().orEmpty(),
                    blackList = emptyList(),
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
                    phoneNumber = phoneNumber,
                    avatarUrl = if (state.value.isEdit) state.value.avatarUri.toString()
                    else null,
                    rating = if (state.value.isEdit) state.value.rating else null
                )

                val newUserId = userProfileRepository.upsertProfile(
                    model = model,
                    avatarUri = avatarUri,
                    isNewUser = !state.value.isEdit,
                )
                finishUpsert(newUserId)
            }
        }
    }

    private suspend fun upsertSpecialist() {
        if (validateSpecialistFields()) {
            with(state.value) {
                val model = UserProfileModel(
                    userId = authRepository.currentUserId().orEmpty(),
                    profileId = userProfileRepository.currentProfileId().orEmpty(),
                    blackList = emptyList(),
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
                    minimalPrice = prices.minOf { it.price },
                    maximalPrice = prices.maxOf { it.price },
                    phoneNumber = phoneNumber,
                    avatarUrl = if (isEdit) avatarUri.toString()
                    else null,
                    portfolioUrls = if (isEdit)
                        portfolioUris.map { it.toString() }.filter { isHttpsUrl(it) }
                    else null,
                    rating = if (isEdit) state.value.rating else null
                )
                val newUserId = userProfileRepository.upsertProfile(
                    model = model,
                    avatarUri = avatarUri,
                    portfolioUris = portfolioUris,
                    isNewUser = !state.value.isEdit,
                )
                finishUpsert(newUserId)
            }
        }
    }

    private fun validateCustomerFields(): Boolean {
        with(state.value) {
            val validationErrors = mutableMapOf<RegistrationScreenInputField, String>()
            if (surname.isBlank())
                validationErrors[RegistrationScreenInputField.SURNAME] =
                    context.getString(R.string.registration_screen_surname_validation_error)
            if (name.isBlank())
                validationErrors[RegistrationScreenInputField.NAME] =
                    context.getString(R.string.registration_screen_name_validation_error)
            if (country.isBlank())
                validationErrors[RegistrationScreenInputField.COUNTRY] =
                    context.getString(R.string.registration_screen_country_validation_error)
            if (city.isBlank())
                validationErrors[RegistrationScreenInputField.CITY] =
                    context.getString(R.string.registration_screen_city_validation_error)
            if (phoneNumber.isNotEmpty() && !isValidPhoneNumber(phoneNumber))
                validationErrors[RegistrationScreenInputField.PHONE_NUMBER] =
                    context.getString(R.string.invalid_phone_number_error)
            if (email.isNotEmpty() && !isValidEmail(email))
                validationErrors[RegistrationScreenInputField.EMAIL] =
                    context.getString(R.string.invalid_email_error)
            if (personalSite.isNotEmpty() && !isValidUrl(personalSite))
                validationErrors[RegistrationScreenInputField.PERSONAL_WEBSITE] =
                    context.getString(R.string.invalid_personal_site_url_error)

            update(
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
            if (avatarUri == null)
                validationErrors[RegistrationScreenInputField.AVATAR] =
                    context.getString(R.string.registration_screen_avatar_validation_error)
            if (surname.isBlank())
                validationErrors[RegistrationScreenInputField.SURNAME] =
                    context.getString(R.string.registration_screen_surname_validation_error)
            if (name.isBlank())
                validationErrors[RegistrationScreenInputField.NAME] =
                    context.getString(R.string.registration_screen_name_validation_error)
            if (specialization.isBlank())
                validationErrors[RegistrationScreenInputField.SPECIALIZATION] =
                    context.getString(R.string.registration_screen_specialization_validation_error)
            if (country.isBlank())
                validationErrors[RegistrationScreenInputField.COUNTRY] =
                    context.getString(R.string.registration_screen_country_validation_error)
            if (city.isBlank())
                validationErrors[RegistrationScreenInputField.CITY] =
                    context.getString(R.string.registration_screen_city_validation_error)
            if (phoneNumber.isNotEmpty() && !isValidPhoneNumber(phoneNumber))
                validationErrors[RegistrationScreenInputField.PHONE_NUMBER] =
                    context.getString(R.string.invalid_phone_number_error)
            if (email.isNotEmpty() && !isValidEmail(email))
                validationErrors[RegistrationScreenInputField.EMAIL] =
                    context.getString(R.string.invalid_email_error)
            if (portfolioUris.isEmpty())
                validationErrors[RegistrationScreenInputField.PORTFOLIO] =
                    context.getString(R.string.registration_screen_portfolio_validation_error)
            if (personalSite.isNotEmpty() && !isValidUrl(personalSite))
                validationErrors[RegistrationScreenInputField.PERSONAL_WEBSITE] =
                    context.getString(R.string.invalid_personal_site_url_error)
            if (prices.isEmpty())
                validationErrors[RegistrationScreenInputField.PRICES] =
                    context.getString(R.string.registration_screen_prices_validation_error)
            update(
                state.value.copy(
                    validationErrors = validationErrors,
                )
            )
            return validationErrors.isEmpty()
        }
    }

    fun onSelectAccountTypeClick(isSpecialist: Boolean) {
        navHostController.navigate(
            LensaScreens.REGISTRATION_SCREEN.name +
                    "/$isSpecialist" +
                    "/$EMPTY_USER_ID"
        )
    }

    fun onDismissClick() {
        if (state.value.isEdit) {
            navHostController.navigate(LensaScreens.SETTINGS_SCREEN.name)
        } else {
            navHostController.navigate(LensaScreens.REGISTRATION_ROLE_SELECTOR_SCREEN.name)
        }
    }
}