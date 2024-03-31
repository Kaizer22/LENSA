package ru.arinae_va.lensa.presentation.feature.settings.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.domain.repository.ISettingsRepository
import ru.arinae_va.lensa.domain.repository.IUserInfoRepository
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import ru.arinae_va.lensa.utils.Constants
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userInfoRepository: IUserInfoRepository,
    private val navHostController: NavHostController,
    private val settingsRepository: ISettingsRepository,
) : ViewModel() {

    fun onAboutClick() = navHostController.navigate(LensaScreens.ABOUT_APP_SCREEN.name)

    fun onBackPressed() = navHostController.popBackStack()

    fun onEditProfileClick() {
        val isSpecialist = userInfoRepository.currentUserProfile()?.specialization !=
                Constants.CUSTOMER_SPECIALIZATION
        navHostController.navigate(
            LensaScreens.REGISTRATION_SCREEN.name +
                    "/$isSpecialist" +
                    "/${userInfoRepository.currentUserId()}"
        )
    }

    fun onThemeSwitched(isDark: Boolean) {

    }

    fun onDeleteAccountClick() {
        viewModelScope.launch {
            userInfoRepository.deleteAccount()
            logOut()
        }
    }

    fun onExitClick() {
        viewModelScope.launch { logOut() }
    }

    private fun logOut() {
        userInfoRepository.logOut()
        settingsRepository.clearUser()
        navHostController.navigate(LensaScreens.AUTH_SCREEN.name)
    }

    fun onFeedbackClick() = navHostController.navigate(
        LensaScreens.FEEDBACK_SCREEN.name
    )

    fun onSendFeedbackClick(text: String) {
        viewModelScope.launch {
            userInfoRepository.sendFeedback(
                userUid = Firebase.auth.uid,
                text = text,
            )
            navHostController.popBackStack()
        }
    }
}