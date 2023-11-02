package ru.arinae_va.lensa.data.repositroy

import ru.arinae_va.lensa.data.datasource.local.ISettingsStorage
import ru.arinae_va.lensa.domain.repository.ISettingsRepository
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    val settingsStorage: ISettingsStorage,
): ISettingsRepository {
    override fun isNeedToShowOnboarding(): Boolean = settingsStorage.isNeedToShowOnboarding

    override fun setOnboardingShown() {
        settingsStorage.isNeedToShowOnboarding = false
    }
}