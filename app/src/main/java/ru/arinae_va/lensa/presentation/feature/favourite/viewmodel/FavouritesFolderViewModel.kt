package ru.arinae_va.lensa.presentation.feature.favourite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.domain.repository.IUserInfoRepository
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import javax.inject.Inject

@HiltViewModel
class FavouritesFolderViewModel @Inject constructor(
    private val userInfoRepository: IUserInfoRepository,
    private val navHostController: NavHostController,
): ViewModel() {

    private val _state = MutableStateFlow(FavouritesFolderState.INITIAL)
    val state: StateFlow<FavouritesFolderState> = _state

    fun loadProfiles(idsInFolder: List<String>) {
        viewModelScope.launch {
            val profiles = userInfoRepository.getProfilesByIds(idsInFolder)
            _state.tryEmit(
                state.value.copy(
                    folderItems = profiles
                )
            )
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
}