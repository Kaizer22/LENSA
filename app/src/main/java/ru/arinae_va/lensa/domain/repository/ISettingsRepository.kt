package ru.arinae_va.lensa.domain.repository

interface ISettingsRepository {
    fun isDarkMode(): Boolean
    fun isNeedToShowOnboarding(): Boolean

    fun lastLoggedInUser(): String?

    fun clearUser()

    fun updateLastLoggedInUser(userId: String)

    fun setOnboardingShown()

    fun setAppTheme(isDark: Boolean)
}