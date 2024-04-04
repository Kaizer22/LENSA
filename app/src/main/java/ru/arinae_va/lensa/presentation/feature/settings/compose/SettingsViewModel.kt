package ru.arinae_va.lensa.presentation.feature.settings.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.domain.repository.IAuthRepository
import ru.arinae_va.lensa.domain.repository.IFeedbackRepository
import ru.arinae_va.lensa.domain.repository.ISettingsRepository
import ru.arinae_va.lensa.domain.repository.IUserProfileRepository
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import ru.arinae_va.lensa.utils.Constants
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userProfileRepository: IUserProfileRepository,
    private val authRepository: IAuthRepository,
    private val feedbackRepository: IFeedbackRepository,
    private val navHostController: NavHostController,
    private val settingsRepository: ISettingsRepository,
) : ViewModel() {

    fun onAboutClick() = navHostController.navigate(LensaScreens.ABOUT_APP_SCREEN.name)

    fun onBackPressed() = navHostController.popBackStack()

    fun onEditProfileClick() {
        val isSpecialist = userProfileRepository.currentUserProfile()?.specialization !=
                Constants.CUSTOMER_SPECIALIZATION
        navHostController.navigate(
            LensaScreens.REGISTRATION_SCREEN.name +
                    "/$isSpecialist" +
                    "/${userProfileRepository.currentProfileId()}"
        )
    }

    fun onThemeSwitched(isDark: Boolean) {

    }

    fun onDeleteAccountClick() {
        viewModelScope.launch {
            userProfileRepository.deleteProfile()
            logOut()
        }
    }

    fun onExitClick() {
        viewModelScope.launch { logOut() }
    }

    private fun logOut() {
        authRepository.logOut()
        settingsRepository.clearUser()
        navHostController.navigate(LensaScreens.AUTH_SCREEN.name)
    }

    fun onFeedbackClick() = navHostController.navigate(
        LensaScreens.FEEDBACK_SCREEN.name
    )

    fun onSendFeedbackClick(text: String) {
        viewModelScope.launch {
            feedbackRepository.sendFeedback(
                userUid = authRepository.currentUserId(),
                text = text,
            )
            navHostController.popBackStack()
        }
    }
}