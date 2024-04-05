package ru.arinae_va.lensa.presentation.feature.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _state = MutableStateFlow(
        SettingsScreenState(
            isLoading = false,
            isShowDeleteProfileDialog = false,
            isShowExitDialog = false,
            isShowSelectProfileDialog = false,
            userProfiles = emptyList(),
        )
    )
    internal val state: StateFlow<SettingsScreenState> = _state

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
            setLoading(true)
            userProfileRepository.deleteProfile()
            setLoading(false)
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

    fun onAddProfileClick() {
        navHostController.navigate(LensaScreens.REGISTRATION_ROLE_SELECTOR_SCREEN.name)
    }

    private fun setLoading(isLoading: Boolean) {
        _state.tryEmit(
            state.value.copy(
                isLoading = isLoading,
            )
        )
    }

    fun showDeleteDialog() {
        _state.tryEmit(
            state.value.copy(
                isShowDeleteProfileDialog = true
            )
        )
    }

    fun hideDeleteDialog() {
        _state.tryEmit(
            state.value.copy(
                isShowDeleteProfileDialog = false
            )
        )
    }

    fun showExitDialog() {
        _state.tryEmit(
            state.value.copy(
                isShowExitDialog = true
            )
        )
    }

    fun hideExitDialog() {
        _state.tryEmit(
            state.value.copy(
                isShowExitDialog = false
            )
        )
    }

    fun showSelectProfileDialog() {
        viewModelScope.launch {
            setLoading(true)
            val profiles = userProfileRepository.getProfilesByUserId(
                userId = authRepository.currentUserId().orEmpty()
            )
            _state.tryEmit(
                state.value.copy(
                    userProfiles = profiles,
                    isShowSelectProfileDialog = true,
                )
            )
            setLoading(false)
        }
    }

    fun hideSelectProfileDialog() {
        _state.tryEmit(
            state.value.copy(
                isShowSelectProfileDialog = false
            )
        )
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