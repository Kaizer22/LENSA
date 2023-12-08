package ru.arinae_va.lensa.presentation.feature.onboarding.compose

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.arinae_va.lensa.domain.repository.ISettingsRepository
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val settingsRepository: ISettingsRepository,
): ViewModel() {
    fun isNeedOnboarding(): Boolean = settingsRepository.isNeedToShowOnboarding()
    fun setOnboardingShown() {
        settingsRepository.setOnboardingShown()
    }
}