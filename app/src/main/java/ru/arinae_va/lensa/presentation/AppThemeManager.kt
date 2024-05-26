package ru.arinae_va.lensa.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.arinae_va.lensa.domain.repository.ISettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppThemeManager @Inject constructor(
    private val settingsRepository: ISettingsRepository,
) {
    private val _isDarkTheme = MutableStateFlow(settingsRepository.isDarkMode())
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    fun isDarkTheme() = settingsRepository.isDarkMode()
    fun setTheme(isDark: Boolean) {
        _isDarkTheme.value = isDark
        settingsRepository.setAppTheme(isDark)
    }
}