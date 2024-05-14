package ru.arinae_va.lensa.presentation.feature.favourite.viewmodel

import ru.arinae_va.lensa.domain.model.user.UserProfileModel

data class FavouritesFolderState(
    val isLoading: Boolean,
    val folderName: String,
    val folderItems: List<UserProfileModel>,
    val idsToDelete: List<String>
) {
    companion object {
        val INITIAL = FavouritesFolderState(
            isLoading = true,
            folderName = "",
            folderItems = emptyList(),
            idsToDelete = emptyList(),
        )
    }
}