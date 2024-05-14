package ru.arinae_va.lensa.presentation.feature.registration.compose.model

import android.net.Uri
import ru.arinae_va.lensa.domain.model.user.Price
import ru.arinae_va.lensa.presentation.feature.feed.compose.SocialMediaType

data class RegistrationScreenData(
    val name: String,
    val surname: String,
    val specialization: String,
    val avatarUri: Uri,
    val country: String,
    val city: String,
    val personalSite: String,
    val email: String,
    val socialMedias: Map<SocialMediaType, String>,
    val about: String,
    val portfolioUris: List<Uri>,
    val prices: List<Price>,
)