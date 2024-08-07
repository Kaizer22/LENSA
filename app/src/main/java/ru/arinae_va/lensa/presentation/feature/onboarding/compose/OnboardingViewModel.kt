package ru.arinae_va.lensa.presentation.feature.onboarding.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.domain.repository.IAuthRepository
import ru.arinae_va.lensa.domain.repository.ISettingsRepository
import ru.arinae_va.lensa.domain.repository.IUserProfileRepository
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val navHostController: NavHostController,
    private val settingsRepository: ISettingsRepository,
    private val userProfileRepository: IUserProfileRepository,
    private val authRepository: IAuthRepository,
) : ViewModel() {

    private var isSuccessMemorizedLogIn = false

    fun tryLogIn() {
        viewModelScope.launch {
            if (!settingsRepository.lastLoggedInUser().isNullOrEmpty()) {
                val rememberedUserId = settingsRepository.lastLoggedInUser()!!
                authRepository.logIn(rememberedUserId)
                isSuccessMemorizedLogIn = true
                //navHostController.navigate(LensaScreens.FEED_SCREEN.name)
            }
        }
    }

    fun onNextClick() {
        settingsRepository.setOnboardingShown()
        navHostController.navigate(LensaScreens.AUTH_SCREEN.name)
    }

    fun onSplashShown() {
        if (settingsRepository.isNeedToShowOnboarding()) {
            navHostController.navigate(LensaScreens.ONBOARDING_SCREEN.name)
        } else {
            if (isSuccessMemorizedLogIn) {
                navHostController.navigate(LensaScreens.FEED_SCREEN.name)
            } else {
                navHostController.navigate(LensaScreens.AUTH_SCREEN.name)
            }
        }
    }
}