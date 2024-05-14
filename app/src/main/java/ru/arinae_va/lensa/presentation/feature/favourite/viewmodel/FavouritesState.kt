package ru.arinae_va.lensa.presentation.feature.favourite.viewmodel

import ru.arinae_va.lensa.domain.model.user.UserProfileModel

data class FavouritesState(
    val isLoading: Boolean,
    val folders: Map<String, List<UserProfileModel>>
) {
    companion object {
        val INITIAL = FavouritesState(
            isLoading = true,
            folders = emptyMap(),
        )
    }
}