package ru.arinae_va.lensa.presentation.feature.favourite.viewmodel

import ru.arinae_va.lensa.domain.model.UserProfileModel

data class FavouritesState(
    val folders: Map<String, List<UserProfileModel>>
) {
    companion object {
        val INITIAL = FavouritesState(
            folders = emptyMap(),
        )
    }
}