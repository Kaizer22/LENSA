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
import ru.arinae_va.lensa.domain.repository.IFavouritesRepository
import ru.arinae_va.lensa.domain.repository.IReviewRepository
import ru.arinae_va.lensa.domain.repository.IUserProfileRepository
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ProfileDetailsViewModel @Inject constructor(
    private val navHostController: NavHostController,
    private val userProfileRepository: IUserProfileRepository,
    private val favouritesRepository: IFavouritesRepository,
    private val reviewRepository: IReviewRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileDetailsState.INITIAL)
    val state: StateFlow<ProfileDetailsState> = _state

    fun loadUserProfile(
        profileUid: String,
        isSelf: Boolean,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            setLoading(true)
            var result = userProfileRepository.getProfileById(profileUid)
            result = result.copy(
                reviews = reviewRepository.getReviewsByProfileId(profileUid)
            )
            _state.tryEmit(
                state.value.copy(
                    userProfileModel = result,
                    isSelf = isSelf,
                    isAddedToFavourites = !isSelf && isProfileAddedToFavourites(profileUid),
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
        favouritesRepository.getFavourites().any { folder ->
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
        val isSelf = userProfileRepository.currentProfileId() == userId
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
            userProfileRepository.currentProfileId()?.let { id ->
                reviewRepository.upsertReview(
                    // TODO refactor
                    targetProfileId = state.value.userProfileModel.profileId,
                    review = Review(
                        authorId = id,
                        profileId = state.value.userProfileModel.profileId,
                        name = userProfileRepository.currentUserProfile()?.name.orEmpty(),
                        surname = userProfileRepository.currentUserProfile()?.surname.orEmpty(),
                        avatarUrl = userProfileRepository.currentUserProfile()?.avatarUrl.orEmpty(),
                        text = state.value.reviewText,
                        rating = state.value.rating,
                        dateTime = LocalDateTime.now(),
                    )
                )
                loadUserProfile(
                    profileUid = state.value.userProfileModel.profileId,
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
                favouritesRepository.addFavourite(
                    profileId = state.value.userProfileModel.profileId,
                    folderName = state.value.userProfileModel.specialization,
                )
            } else {
                favouritesRepository.removeFavourite(
                    profileId = state.value.userProfileModel.profileId,
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