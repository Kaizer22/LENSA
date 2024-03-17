package ru.arinae_va.lensa.presentation.feature.feed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.domain.repository.IUserInfoRepository
import javax.inject.Inject

@HiltViewModel
class ProfileDetailsViewModel @Inject constructor(
    private val userInfoRepository: IUserInfoRepository
): ViewModel() {
    private val _state = MutableStateFlow(ProfileDetailsState.INITIAL)
    val state: StateFlow<ProfileDetailsState> = _state

    fun loadUserProfile(
        userUid: String,
        isSelf: Boolean,
    ) {
        viewModelScope.launch {
            val result = userInfoRepository.getProfileById(userUid)
            _state.tryEmit(
                state.value.copy(
                    userProfileModel = result,
                    isSelf = isSelf,
                )
            )
        }
    }
}