package ru.arinae_va.lensa.presentation.feature.feed.viewmodel

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.domain.model.Review
import ru.arinae_va.lensa.domain.repository.IChatRequestRepository
import ru.arinae_va.lensa.domain.repository.IFavouritesRepository
import ru.arinae_va.lensa.domain.repository.IReviewRepository
import ru.arinae_va.lensa.domain.repository.IUserProfileRepository
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ProfileDetailsViewModel @Inject constructor(
    private val navHostController: NavHostController,
    private val snackbarHostState: MutableState<SnackbarHostState>,
    private val chatRequestRepository: IChatRequestRepository,
    private val userProfileRepository: IUserProfileRepository,
    private val favouritesRepository: IFavouritesRepository,
    private val reviewRepository: IReviewRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileDetailsState.INITIAL)
    val state: StateFlow<ProfileDetailsState> = _state

    fun loadUserProfile(
        profileUid: String,
        isSelf: Boolean,
        isNeedToScrollToReviews: Boolean = false,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            setLoading(true)
            var result = userProfileRepository.getProfileById(profileUid)
            val reviews = reviewRepository.getReviewsByProfileId(profileUid)
            result = result.copy(
                reviews = reviews,
                rating = calculateRating(reviews)
            )
            _state.tryEmit(
                state.value.copy(
                    userProfileModel = result,
                    isNeedToScrollToReviews = isNeedToScrollToReviews,
                    isSelf = isSelf,
                    isAddedToFavourites = !isSelf && isProfileAddedToFavourites(profileUid),
                )
            )
            setLoading(false)
        }
    }

    private fun calculateRating(reviews: List<Review>): Float = if (reviews.isNotEmpty())
        reviews.map { it.rating }.sum() / reviews.size
    else 0.0f

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
            setLoading(true)
            userProfileRepository.currentProfileId()?.let { id ->
                val review = Review(
                    authorId = id,
                    profileId = state.value.userProfileModel.profileId,
                    name = userProfileRepository.currentUserProfile()?.name.orEmpty(),
                    surname = userProfileRepository.currentUserProfile()?.surname.orEmpty(),
                    avatarUrl = userProfileRepository.currentUserProfile()?.avatarUrl.orEmpty(),
                    text = state.value.reviewText,
                    rating = state.value.rating,
                    dateTime = LocalDateTime.now(),
                )
                reviewRepository.upsertReview(
                    // TODO refactor
                    targetProfileId = state.value.userProfileModel.profileId,
                    review = review
                )
                loadReviews()
                // TODO calculate rating on backend
                val newRating = calculateRating(state.value.userProfileModel.reviews ?: emptyList())
                userProfileRepository.updateRating(
                    rating = newRating,
                    profileId = state.value.userProfileModel.profileId
                )
                _state.tryEmit(
                    state.value.copy(
                        userProfileModel = state.value.userProfileModel.copy(
                            rating = newRating
                        )
                    )
                )
                setLoading(false)
            }
        }
    }

    private suspend fun loadReviews() {
        val reviews =
            reviewRepository.getReviewsByProfileId(state.value.userProfileModel.profileId)
        _state.tryEmit(
            state.value.copy(
                userProfileModel = state.value.userProfileModel.copy(
                    reviews = reviews,
                ),
                // send event?
                isNeedToScrollToReviews = true,
            )
        )
    }

    fun onChatsClick() {
        TODO("Not yet implemented")
    }

    fun onSendMessageClick(recipientUserId: String) {
        viewModelScope.launch {
            chatRequestRepository.sendChatRequest(recipientUserId)
            snackbarHostState.value.showSnackbar(
                message = "Запрос отправлен"
            )
        }
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