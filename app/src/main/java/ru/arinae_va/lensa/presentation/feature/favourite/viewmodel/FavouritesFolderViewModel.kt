package ru.arinae_va.lensa.presentation.feature.favourite.viewmodel

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.domain.repository.IFavouritesRepository
import ru.arinae_va.lensa.domain.repository.IUserProfileRepository
import ru.arinae_va.lensa.presentation.common.StateViewModel
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import javax.inject.Inject

@HiltViewModel
class FavouritesFolderViewModel @Inject constructor(
    private val snackbarHostState: MutableState<SnackbarHostState>,
    private val userProfileRepository: IUserProfileRepository,
    private val favouritesRepository: IFavouritesRepository,
    private val navHostController: NavHostController,
) : StateViewModel<FavouritesFolderState>(
    initialState = FavouritesFolderState.INITIAL,
) {

    fun loadProfiles(folderName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // TODO remove db usage
            setLoading(true)
            //favouritesRepository.dropTable()
            favouritesRepository.getFavourites()
                .find { it.name == folderName }
                ?.savedUserIds
                ?.let { idsInFolder ->
                    val profiles = userProfileRepository.getProfilesByIds(idsInFolder)
                    updateSuspending(
                        state.value.copy(
                            folderName = folderName,
                            folderItems = profiles,
                        )
                    )
                }
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

    fun onProfileClicked(userId: String) {
        val isSelf = false
        navHostController.navigate(
            "${LensaScreens.SPECIALIST_DETAILS_SCREEN.name}/" +
                    "${userId}/" +
                    "$isSelf"
        )
    }

    fun onRemoveProfileClicked(profileId: String, isRemove: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isRemove) {
                favouritesRepository.removeFavourite(
                    profileId = profileId,
                    folderName = state.value.folderName,
                )
                snackbarHostState.value.showSnackbar("Удалено из избранного")
            } else {
                favouritesRepository.addFavourite(
                    profileId = profileId,
                    folderName = state.value.folderName,
                )
                snackbarHostState.value.showSnackbar("Добавлено в избранное")
            }

        }

//        val mutableIdsList = state.value.idsToDelete.toMutableList()
//        if (isRemove) mutableIdsList += userId
//        else mutableIdsList -= userId
//        update(
//            state.value.copy(
//                idsToDelete = mutableIdsList
//            )
//        )
    }

    fun onBackPressed() {
        navHostController.popBackStack()
    }
}