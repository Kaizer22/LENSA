package ru.arinae_va.lensa.presentation.feature.favourite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.domain.repository.IFavouritesRepository
import ru.arinae_va.lensa.domain.repository.IUserProfileRepository
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import javax.inject.Inject

@HiltViewModel
class FavouritesFolderViewModel @Inject constructor(
    private val userProfileRepository: IUserProfileRepository,
    private val favouritesRepository: IFavouritesRepository,
    private val navHostController: NavHostController,
) : ViewModel() {

    private val _state = MutableStateFlow(FavouritesFolderState.INITIAL)
    val state: StateFlow<FavouritesFolderState> = _state

    fun loadProfiles(folderName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // TODO remove db usage
            favouritesRepository.getFavourites()
                .find { it.name == folderName }
                ?.savedUserIds
                ?.let { idsInFolder ->
                    val profiles = userProfileRepository.getProfilesByIds(idsInFolder)
                    _state.tryEmit(
                        state.value.copy(
                            folderName = folderName,
                            folderItems = profiles,
                        )
                    )
                }
        }
    }

    fun onProfileClicked(userId: String) {
        val isSelf = false
        navHostController.navigate(
            "${LensaScreens.SPECIALIST_DETAILS_SCREEN.name}/" +
                    "${userId}/" +
                    "$isSelf"
        )
    }

    fun onRemoveProfileClicked(userId: String, isRemove: Boolean) {
        val mutableIdsList = state.value.idsToDelete.toMutableList()
        if (isRemove) mutableIdsList += userId
        else mutableIdsList -= userId
        _state.tryEmit(
            state.value.copy(
                idsToDelete = mutableIdsList
            )
        )
    }

    fun onBackPressed() {
        navHostController.popBackStack()
    }
}