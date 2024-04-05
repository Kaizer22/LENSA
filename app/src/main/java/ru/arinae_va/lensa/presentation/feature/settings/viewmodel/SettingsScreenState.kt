package ru.arinae_va.lensa.presentation.feature.settings.viewmodel

import ru.arinae_va.lensa.domain.model.UserProfileModel

data class SettingsScreenState(
    val userProfiles: List<UserProfileModel>,
    val isLoading: Boolean,
    val isShowDeleteProfileDialog: Boolean,
    val isShowExitDialog: Boolean,
    val isShowSelectProfileDialog: Boolean,
)
