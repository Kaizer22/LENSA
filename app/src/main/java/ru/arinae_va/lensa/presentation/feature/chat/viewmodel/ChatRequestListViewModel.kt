package ru.arinae_va.lensa.presentation.feature.chat.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.domain.model.ChatRequest
import ru.arinae_va.lensa.domain.repository.IChatRequestRepository
import ru.arinae_va.lensa.domain.repository.IUserProfileRepository
import ru.arinae_va.lensa.presentation.common.StateViewModel
import javax.inject.Inject

@HiltViewModel
class ChatRequestListViewModel @Inject constructor(
    private val navHostController: NavHostController,
    private val userProfileRepository: IUserProfileRepository,
    private val chatRequestRepository: IChatRequestRepository,
) : StateViewModel<ChatRequestListState>(
    initialState = ChatRequestListState(
        chatRequests = emptyList(),
        isLoading = true,
    )
) {

    private fun setLoading(isLoading: Boolean) {
        update(
            state.value.copy(
                isLoading = isLoading,
            )
        )
    }

    fun onAttach() {
        viewModelScope.launch(Dispatchers.IO) {
            userProfileRepository.currentProfileId()?.let { currentProfileId ->
                chatRequestRepository.getChatRequests(
                    profileId = currentProfileId,
                ).collectLatest { newList ->
                    setLoading(true)
                    update(
                        state.value.copy(
                            chatRequests = newList,
                            isLoading = false,
                        )
                    )
                }
            }
        }
    }

    fun onBackPressed() {
        navHostController.popBackStack()
    }

    fun onAcceptRequest(chatRequest: ChatRequest) {
        viewModelScope.launch {
            chatRequestRepository.approveChatRequest(chatRequest)
        }
    }

    fun onCancelRequest(chatRequest: ChatRequest) {
        viewModelScope.launch {
            chatRequestRepository.cancelChatRequest(chatRequest)
        }
    }
}