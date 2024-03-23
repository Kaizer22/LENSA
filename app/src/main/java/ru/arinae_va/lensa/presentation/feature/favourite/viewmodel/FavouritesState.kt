package ru.arinae_va.lensa.presentation.feature.favourite.viewmodel

import ru.arinae_va.lensa.data.model.FavouriteFolder

data class FavouritesState(
    val folders: List<FavouriteFolder>
) {
    companion object {
        val INITIAL = FavouritesState(
            folders = emptyList(),
        )
    }
}