package ru.arinae_va.lensa.presentation.feature.registration.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.domain.model.SocialMedia
import ru.arinae_va.lensa.domain.model.SpecialistModel
import ru.arinae_va.lensa.domain.repository.IUserInfoRepository
import ru.arinae_va.lensa.presentation.feature.registration.compose.model.RegistrationScreenData
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val navHostController: NavHostController,
    private val userInfoRepository: IUserInfoRepository,
) : ViewModel() {

    fun onGetInTouchClick() {
        navHostController.navigate(LensaScreens.FEEDBACK_SCREEN.name)
    }

    // TODO отображение загрузки
    fun onSaveClick(data: RegistrationScreenData) {
        viewModelScope.launch {
            val model = SpecialistModel(
                id = Firebase.auth.currentUser?.uid ?: "",
                name = data.name,
                surname = data.surname,
                specialization = data.specialization,
                country = data.country,
                city = data.city,
                personalSite = data.personalSite,
                email = data.email,
                about = data.about,
                socialMedias = data.socialMedias.map {
                    SocialMedia(
                        link = it.value,
                        type = it.key,
                    )
                },
                prices = data.prices,
            )
            userInfoRepository.upsertProfile(
                model = model,
                avatarUri = data.avatarUri,
                portfolioUris = data.portfolioUris,
                isNewUser = true,
            )
            navHostController.navigate(LensaScreens.FEED_SCREEN.name)
        }
    }

    fun onSelectAccountTypeClick(isSpecialist: Boolean) {
        navHostController.navigate("${LensaScreens.REGISTRATION_SCREEN.name}/$isSpecialist")
    }
}