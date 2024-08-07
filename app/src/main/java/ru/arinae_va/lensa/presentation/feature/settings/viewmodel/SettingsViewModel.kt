package ru.arinae_va.lensa.presentation.feature.settings.viewmodel

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.domain.repository.IAuthRepository
import ru.arinae_va.lensa.domain.repository.IFeedbackRepository
import ru.arinae_va.lensa.domain.repository.ISettingsRepository
import ru.arinae_va.lensa.domain.repository.IUserProfileRepository
import ru.arinae_va.lensa.presentation.AppThemeManager
import ru.arinae_va.lensa.presentation.common.StateViewModel
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import ru.arinae_va.lensa.utils.Constants
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appThemeManager: AppThemeManager,
    private val userProfileRepository: IUserProfileRepository,
    private val authRepository: IAuthRepository,
    private val feedbackRepository: IFeedbackRepository,
    private val navHostController: NavHostController,
    private val settingsRepository: ISettingsRepository,
) : StateViewModel<SettingsScreenState>(
    initialState = SettingsScreenState(
        isDarkModeEnabled = appThemeManager.isDarkTheme(),
        isLoading = false,
        isShowDeleteProfileDialog = false,
        isShowExitDialog = false,
        isShowSelectProfileDialog = false,
        userProfiles = emptyList(),
        selectProfileDialogDismissButtonText = "ОТМЕНИТЬ"
    )
) {

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
        update(
            state.value.copy(
                isDarkModeEnabled = isDark,
            )
        )
        appThemeManager.setTheme(isDark)
    }

    fun onDeleteAccountClick() {
        viewModelScope.launch {
            setLoading(true)
            userProfileRepository.deleteProfile()
            val currentUserId = authRepository.currentUserId()
            logOut()
            hideDeleteDialog()
            showSelectProfileDialog(
                currentUserId = currentUserId,
                showScreenAfterwards = false,
                exitDismissButtonText = true,
            )
        }
    }

    fun onExitClick() {
        viewModelScope.launch {
            val userId = authRepository.currentUserId()
            logOut()
            hideExitDialog()
            showSelectProfileDialog(
                currentUserId = userId,
                showScreenAfterwards = false,
                exitDismissButtonText = true,
            )
        }
    }

    private fun logOut() {
        authRepository.logOut()
        settingsRepository.clearUser()
    }

    private fun toAuthScreen() {
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

    fun onAddProfileClick() {
        navHostController.navigate(LensaScreens.REGISTRATION_ROLE_SELECTOR_SCREEN.name)
    }

    private fun setLoading(isLoading: Boolean) {
        update(
            state.value.copy(
                isLoading = isLoading,
            )
        )
    }

    fun showDeleteDialog() {
        update(
            state.value.copy(
                isShowDeleteProfileDialog = true
            )
        )
    }

    fun hideDeleteDialog() {
        update(
            state.value.copy(
                isShowDeleteProfileDialog = false
            )
        )
    }

    fun showExitDialog() {
        update(
            state.value.copy(
                isShowExitDialog = true
            )
        )
    }

    fun hideExitDialog() {
        update(
            state.value.copy(
                isShowExitDialog = false
            )
        )
    }

    fun showSelectProfileDialog(
        currentUserId: String? = null,
        showScreenAfterwards: Boolean = true,
        exitDismissButtonText: Boolean = false,
    ) {
        viewModelScope.launch {
            setLoading(true)
            val userId = currentUserId ?: authRepository.currentUserId().orEmpty()
            val profiles = userProfileRepository.getProfilesByUserId(
                userId = userId
            )
            update(
                state.value.copy(
                    userProfiles = profiles,
                    isShowSelectProfileDialog = true,
                    selectProfileDialogDismissButtonText = if (exitDismissButtonText) {
                        "ВЫХОД"
                    } else {
                        "ОТМЕНИТЬ"
                    }
                )
            )
            if (showScreenAfterwards) {
                setLoading(false)
            }
        }
    }

    fun hideSelectProfileDialog() {
        if (authRepository.currentUserId().isNullOrEmpty()) {
            toAuthScreen()
        } else {
            update(
                state.value.copy(
                    isShowSelectProfileDialog = false
                )
            )
        }
    }

    fun onSelectProfile(profileId: String) {
        viewModelScope.launch {
            setLoading(true)
            authRepository.logOut(
                // Не вызываем signOut Firebase
                isFullLogOut = false,
            )
            authRepository.logIn(profileId)
            navHostController.navigate(LensaScreens.FEED_SCREEN.name)
            setLoading(false)
        }
    }
}