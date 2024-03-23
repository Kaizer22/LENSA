package ru.arinae_va.lensa.presentation.feature.feed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.domain.model.Review
import ru.arinae_va.lensa.domain.repository.IUserInfoRepository
import java.time.LocalDateTime
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

    fun onRatingChanged(rating: Float) {
        _state.tryEmit(
            state.value.copy(
                rating = rating
            )
        )
    }

    fun onReviewAvatarClicked(userId: String) {
        TODO("Not yet implemented")
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
            userInfoRepository.postReview(
                targetUserId = state.value.userProfileModel.id,
                review = Review(
                    authorId = Firebase.auth.currentUser?.uid.orEmpty(),
                    name = state.value.currentUserName,
                    surname = state.value.currentUserSurname,
                    avatarUrl = state.value.currentUserAvatarUrl,
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