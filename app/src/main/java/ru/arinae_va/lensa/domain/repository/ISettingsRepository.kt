package ru.arinae_va.lensa.domain.repository

interface ISettingsRepository {
    fun isNeedToShowOnboarding(): Boolean
    fun setOnboardingShown()
}