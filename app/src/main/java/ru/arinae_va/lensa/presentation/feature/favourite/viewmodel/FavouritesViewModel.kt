package ru.arinae_va.lensa.presentation.feature.favourite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.domain.repository.IUserInfoRepository
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val userInfoRepository: IUserInfoRepository
): ViewModel() {

    private val _state = MutableStateFlow(FavouritesState.INITIAL)
    val state: StateFlow<FavouritesState> = _state

    fun loadFolders() {
        viewModelScope.launch {
            val folders = userInfoRepository.getFavourites()
            _state.tryEmit(
                state.value.copy(
                    folders = folders,
                )
            )
        }
    }

    fun onFolderClicked() {

    }
}