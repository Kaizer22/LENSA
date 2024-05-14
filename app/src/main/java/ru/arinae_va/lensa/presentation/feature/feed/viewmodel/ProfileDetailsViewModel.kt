package ru.arinae_va.lensa.presentation.feature.feed.viewmodel

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.domain.model.chats.Chat
import ru.arinae_va.lensa.domain.model.chats.DialogData
import ru.arinae_va.lensa.domain.model.user.Review
import ru.arinae_va.lensa.domain.repository.IAuthRepository
import ru.arinae_va.lensa.domain.repository.IChatRepository
import ru.arinae_va.lensa.domain.repository.IFavouritesRepository
import ru.arinae_va.lensa.domain.repository.IReviewRepository
import ru.arinae_va.lensa.domain.repository.IUserProfileRepository
import ru.arinae_va.lensa.presentation.common.StateViewModel
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import ru.arinae_va.lensa.utils.Constants
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ProfileDetailsViewModel @Inject constructor(
    private val navHostController: NavHostController,
    private val snackbarHostState: MutableState<SnackbarHostState>,
    //private val chatRequestRepository: IChatRequestRepository,
    private val authRepository: IAuthRepository,
    private val chatRepository: IChatRepository,
    private val userProfileRepository: IUserProfileRepository,
    private val favouritesRepository: IFavouritesRepository,
    private val reviewRepository: IReviewRepository,
) : StateViewModel<ProfileDetailsState>(
    initialState = ProfileDetailsState.INITIAL,
) {

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
            update(
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
        update(
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
        update(
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
        update(
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
                update(
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
        update(
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
        navHostController.navigate(LensaScreens.CHAT_LIST_SCREEN.name)
    }

    fun onSendMessageClick(recipientProfileId: String) {
        viewModelScope.launch {
            // TODO Проверка, что чат уже существует
            userProfileRepository.currentUserProfile()?.let { currentProfile ->
                if (state.value.userProfileModel.blackList.contains(currentProfile.profileId)) {
                    snackbarHostState.value.showSnackbar(
                        message = "Данный пользователь добавил вас в черный список",
                        withDismissAction = true,
                        duration = SnackbarDuration.Long,
                    )
                } else if (currentProfile.blackList.contains(recipientProfileId)) {
                    val result = snackbarHostState.value.showSnackbar(
                        message = "Данный пользователь у вас в черном списке",
                        actionLabel = "РАЗБЛОКИРОВАТЬ",
                        withDismissAction = true,
                        duration = SnackbarDuration.Long,
                    )
                    when (result) {
                        SnackbarResult.ActionPerformed -> {
                            userProfileRepository.removeProfileFromBlackList(recipientProfileId)
                        }
                        SnackbarResult.Dismissed -> {}
                    }
                }
                else {
                    val chatId = getChatId(
                        currentUserId = currentProfile.profileId,
                        recipientId = recipientProfileId,
                    )
                    if (chatRepository.isChatExist(chatId)) {
                        navHostController.navigate(
                            LensaScreens.CHAT_SCREEN.name + "/${chatId}"
                        )
                    } else {
                        val chat = Chat(
                            chatId = chatId,
                            creatorProfileId = currentProfile.profileId,
                            members = listOf(
                                currentProfile.profileId,
                                recipientProfileId,
                            ),
                            name = "",
                            avatarUrl = "",
                            createTime = LocalDateTime.now(),
                            dialogData = DialogData(
                                authorMemberName = currentProfile.profileId,
                                authorAvatarUrl = currentProfile.avatarUrl,
                                targetMemberName = state.value.userProfileModel.name + " " +
                                        state.value.userProfileModel.surname,
                                targetAvatarUrl = state.value.userProfileModel.avatarUrl,
                                targetSpecialization = state.value.userProfileModel.specialization,
                                authorSpecialization = currentProfile.specialization,
                            )
                        )
                        chatRepository.upsertChat(chat)
                        val result = snackbarHostState.value.showSnackbar(
                            message = "Чат создан",
                            actionLabel = "ПЕРЕЙТИ",
                            withDismissAction = true,
                            duration = SnackbarDuration.Long,
                        )
                        when (result) {
                            SnackbarResult.ActionPerformed -> navHostController.navigate(
                                LensaScreens.CHAT_SCREEN.name + "/${chatId}"
                            )
                            SnackbarResult.Dismissed -> {}
                        }
                    }
                }
            }
        }
    }

    private fun getChatId(currentUserId: String, recipientId: String): String {
        val ids = arrayOf(currentUserId, recipientId)
        ids.sort()
        return "${ids[0]}_${ids[1]}"
    }

    fun onAddToFavouritesClick(isNeedToAdd: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isNeedToAdd) {
                favouritesRepository.addFavourite(
                    profileId = state.value.userProfileModel.profileId,
                    folderName = Constants.SPECIALIZATIONS_LIST.find {
                        it.first == state.value.userProfileModel.specialization
                    }?.second ?: "Специалисты",
                )
            } else {
                favouritesRepository.removeFavourite(
                    profileId = state.value.userProfileModel.profileId,
                    folderName = state.value.userProfileModel.specialization,
                )
            }
            update(
                state.value.copy(
                    isAddedToFavourites = isNeedToAdd,
                )
            )
        }
        viewModelScope.launch {
            val result = snackbarHostState.value.showSnackbar(
                message = if (isNeedToAdd) "Добавлено в избранное"
                else "Удалено из избранного",
                actionLabel = "ПЕРЕЙТИ",
                withDismissAction = true,
                duration = SnackbarDuration.Long,
            )
            when (result) {
                SnackbarResult.ActionPerformed -> navHostController.navigate(
                    LensaScreens.FAVOURITES_SCREEN.name
                )

                SnackbarResult.Dismissed -> {}
            }
        }
    }
}