package ru.arinae_va.lensa.data.repositroy

import ru.arinae_va.lensa.data.datasource.local.ISettingsStorage
import ru.arinae_va.lensa.domain.repository.ISettingsRepository
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val settingsStorage: ISettingsStorage,
): ISettingsRepository {
    override fun isDarkMode(): Boolean = settingsStorage.isDarkModeOn

    override fun isNeedToShowOnboarding(): Boolean = settingsStorage.isNeedToShowOnboarding
    override fun lastLoggedInUser(): String? = settingsStorage.lastLoggedInUser

    override fun clearUser() {
        settingsStorage.lastLoggedInUser = null
    }

    override fun updateLastLoggedInUser(userId: String) {
        settingsStorage.lastLoggedInUser = userId
    }

    override fun setOnboardingShown() {
        settingsStorage.isNeedToShowOnboarding = false
    }

    override fun setAppTheme(isDark: Boolean) {
        settingsStorage.isDarkModeOn = isDark
    }
}