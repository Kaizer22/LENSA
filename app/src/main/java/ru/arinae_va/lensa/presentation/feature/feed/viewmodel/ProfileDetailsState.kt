package ru.arinae_va.lensa.presentation.feature.feed.viewmodel

import ru.arinae_va.lensa.domain.model.user.UserProfileModel

data class ProfileDetailsState(
    val userProfileModel: UserProfileModel,
    val isNeedToScrollToReviews: Boolean,
    val reviewText: String,
    val rating: Float,
    val isLoading: Boolean,
    val isSelf: Boolean,
    val isAddedToFavourites: Boolean,
) {
    companion object {
        val INITIAL = ProfileDetailsState(
            userProfileModel = UserProfileModel.EMPTY,
            isNeedToScrollToReviews = false,
            reviewText = "",
            rating = 0f,
            isSelf = false,
            isLoading = false,
            isAddedToFavourites = false,
        )
    }
}