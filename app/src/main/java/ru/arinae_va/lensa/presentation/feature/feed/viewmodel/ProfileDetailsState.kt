package ru.arinae_va.lensa.presentation.feature.feed.viewmodel

import ru.arinae_va.lensa.domain.model.UserProfileModel

data class ProfileDetailsState(
    val userProfileModel: UserProfileModel,
    val isSelf: Boolean,
) {
    companion object {
        val INITIAL = ProfileDetailsState(
            userProfileModel = UserProfileModel.EMPTY,
            isSelf = false,
        )
    }
}