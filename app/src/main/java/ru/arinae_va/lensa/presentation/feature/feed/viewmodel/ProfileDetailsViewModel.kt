package ru.arinae_va.lensa.presentation.feature.feed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.domain.model.Review
import ru.arinae_va.lensa.domain.repository.IUserInfoRepository
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ProfileDetailsViewModel @Inject constructor(
    private val navHostController: NavHostController,
    private val userInfoRepository: IUserInfoRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileDetailsState.INITIAL)
    val state: StateFlow<ProfileDetailsState> = _state

    fun loadUserProfile(
        userUid: String,
        isSelf: Boolean,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            setLoading(true)
            val result = userInfoRepository.getProfileById(userUid)
            _state.tryEmit(
                state.value.copy(
                    userProfileModel = result,
                    isSelf = isSelf,
                    isAddedToFavourites = !isSelf && isProfileAddedToFavourites(userUid),
                )
            )
            setLoading(false)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        _state.tryEmit(
            state.value.copy(
                isLoading = isLoading
            )
        )
    }

    private suspend fun isProfileAddedToFavourites(userUid: String) =
        userInfoRepository.getFavourites().any { folder ->
            folder.savedUserIds.contains(userUid)
        }

    fun onRatingChanged(rating: Float) {
        _state.tryEmit(
            state.value.copy(
                rating = rating
            )
        )
    }

    fun onReviewAvatarClicked(userId: String) {
        val isSelf = userInfoRepository.currentUserId() == userId
        navHostController.navigate(
            "${LensaScreens.SPECIALIST_DETAILS_SCREEN.name}/" +
                    "${userId}/" +
                    "$isSelf"
        )
    }

    fun onReviewChanged(reviewText: String) {
        _state.tryEmit(
            state.value.copy(
                reviewText = reviewText
            )
        )
    }

    fun onPostReview() {
        viewModelScope.launch {
            userInfoRepository.currentUserId()?.let { id ->
                userInfoRepository.postReview(
                    targetUserId = state.value.userProfileModel.id,
                    review = Review(
                        authorId = id,
                        name = userInfoRepository.currentUserProfile()?.name.orEmpty(),
                        surname = userInfoRepository.currentUserProfile()?.surname.orEmpty(),
                        avatarUrl = userInfoRepository.currentUserProfile()?.avatarUrl.orEmpty(),
                        text = state.value.reviewText,
                        rating = state.value.rating,
                        dateTime = LocalDateTime.now(),
                    )
                )
                loadUserProfile(
                    userUid = state.value.userProfileModel.id,
                    isSelf = state.value.isSelf
                )
            }
        }
    }

    fun onChatsClick() {
        TODO("Not yet implemented")
    }

    fun onSendMessageClick(recipientUserId: String) {
        TODO("Not yet implemented")
    }

    fun onAddToFavouritesClick(isNeedToAdd: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isNeedToAdd) {
                userInfoRepository.addFavourite(
                    userId = state.value.userProfileModel.id,
                    folderName = state.value.userProfileModel.specialization,
                )
            } else {
                userInfoRepository.removeFavourite(
                    userId = state.value.userProfileModel.id,
                    folderName = state.value.userProfileModel.specialization,
                )
            }
            _state.tryEmit(
                state.value.copy(
                    isAddedToFavourites = isNeedToAdd,
                )
            )
        }
    }
}