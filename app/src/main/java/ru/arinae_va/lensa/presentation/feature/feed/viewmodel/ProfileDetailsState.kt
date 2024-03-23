package ru.arinae_va.lensa.presentation.feature.feed.viewmodel

import ru.arinae_va.lensa.domain.model.UserProfileModel

data class ProfileDetailsState(
    val userProfileModel: UserProfileModel,
    val currentUserName: String,
    val currentUserSurname: String,
    val currentUserAvatarUrl: String,
    val reviewText: String,
    val rating: Float,
    val isSelf: Boolean,
) {
    companion object {
        val INITIAL = ProfileDetailsState(
            userProfileModel = UserProfileModel.EMPTY,
            currentUserName = "",
            currentUserSurname = "",
            currentUserAvatarUrl = "",
            reviewText = "",
            rating = 0f,
            isSelf = false,
        )
    }
}