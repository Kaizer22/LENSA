package ru.arinae_va.lensa.presentation.feature.favourite.viewmodel

import ru.arinae_va.lensa.domain.model.UserProfileModel

data class FavouritesFolderState(
    val folderName: String,
    val folderItems: List<UserProfileModel>,
    val idsToDelete: List<String>
) {
    companion object {
        val INITIAL = FavouritesFolderState(
            folderName = "",
            folderItems = emptyList(),
            idsToDelete = emptyList(),
        )
    }
}