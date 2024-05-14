package ru.arinae_va.lensa.presentation.feature.favourite.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.domain.model.user.UserProfileModel
import ru.arinae_va.lensa.domain.repository.IFavouritesRepository
import ru.arinae_va.lensa.domain.repository.IUserProfileRepository
import ru.arinae_va.lensa.presentation.common.StateViewModel
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val userProfileRepository: IUserProfileRepository,
    private val favouritesRepository: IFavouritesRepository,
    private val navHostController: NavHostController,
) : StateViewModel<FavouritesState>(
    initialState = FavouritesState.INITIAL,
) {

    fun loadFolders() {
        favouritesRepository
        viewModelScope.launch(Dispatchers.IO) {
            setLoading(true)
            val foldersMap = mutableMapOf<String, List<UserProfileModel>>()
            favouritesRepository.getFavourites().forEach { folder ->
                val users = userProfileRepository.getProfilesByIds(folder.savedUserIds)
                foldersMap[folder.name] = users
            }
            updateSuspending(
                state.value.copy(
                    folders = foldersMap,
                )
            )
            setLoading(false)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        update(
            state.value.copy(
                isLoading = isLoading,
            )
        )
    }

    fun onFolderClicked(folderName: String) {
        navHostController.navigate(
            "${LensaScreens.FAVOURITE_FOLDER_SCREEN.name}/" + folderName
        )
    }

    fun onBackPressed() {
        navHostController.popBackStack()
    }
}