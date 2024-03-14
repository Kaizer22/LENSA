package ru.arinae_va.lensa.presentation.feature.registration.viewmodel

import android.net.Uri
import ru.arinae_va.lensa.domain.model.Price
import ru.arinae_va.lensa.presentation.feature.feed.compose.SocialMediaType
import ru.arinae_va.lensa.utils.Constants

internal enum class RegistrationScreenInputField {
    NAME, SURNAME, SPECIALIZATION, AVATAR,
    COUNTRY, CITY, PHONE_NUMBER, EMAIL, ABOUT,

    SOCIAL_MEDIA_INSTAGRAM, SOCIAL_MEDIA_TELEGRAM,
    SOCIAL_MEDIA_VK, SOCIAL_MEDIA_WHATSAPP, SOCIAL_MEDIA_PINTEREST,
    SOCIAL_MEDIA_YOUTUBE, SOCIAL_MEDIA_BEHANCE,

    PRICES

}

internal data class RegistrationScreenState(
    val isSpecialistRegistrationScreen: Boolean,
    val name: String,
    val surname: String,
    val specialization: String,
    val avatarUri: Uri?,
    val country: String,
    val city: String,
    val personalSite: String,
    val email: String,
    val socialMedias: Map<SocialMediaType, String>,
    val about: String,
    val phoneNumber: String,
    val portfolioUris: List<Uri>,
    val prices: List<Price>,
    val validationErrors: Map<RegistrationScreenInputField, String>,
    val isButtonNextEnabled: Boolean,
) {
    companion object {
        val INITIAL = RegistrationScreenState(
            isSpecialistRegistrationScreen = true,
            name = "",
            surname = "",
            specialization = "",
            avatarUri = null,
            country = "",
            city = "",
            personalSite = "",
            email = "",
            socialMedias = emptyMap(),
            about = "",
            portfolioUris = emptyList(),
            prices = emptyList(),
            phoneNumber = Constants.RUSSIA_COUNTRY_CODE,
            validationErrors = emptyMap(),
            isButtonNextEnabled = false,
        )
    }
}