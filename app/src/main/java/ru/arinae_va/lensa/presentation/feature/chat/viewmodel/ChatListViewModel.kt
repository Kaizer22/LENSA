package ru.arinae_va.lensa.presentation.feature.chat.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.domain.model.Chat
import ru.arinae_va.lensa.domain.model.Message
import ru.arinae_va.lensa.domain.repository.IChatRepository
import ru.arinae_va.lensa.domain.repository.IMessageRepository
import ru.arinae_va.lensa.domain.repository.IUserProfileRepository
import ru.arinae_va.lensa.presentation.common.StateViewModel
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val navHostController: NavHostController,
    private val chatRepository: IChatRepository,
    private val messageRepository: IMessageRepository,
    private val userProfileRepository: IUserProfileRepository,
) : StateViewModel<ChatListState>(
    initialState = ChatListState(
        currentUserId = userProfileRepository.currentProfileId().orEmpty(),
        chats = emptyList(),
        latestMessages = emptyList(),
        isLoading = true,
    )
) {

    fun onAttach() {
        userProfileRepository.currentProfileId()?.let { currentProfileId ->
            viewModelScope.launch(Dispatchers.IO) {
                val chats = chatRepository.getChats(currentProfileId)
                observeChats(chats)
            }
            viewModelScope.launch(Dispatchers.IO) {
                val messages = messageRepository.getLastMessages(
                    state.value.chats.map { it.chatId }
                )
                observeMessages(messages)
            }
        }
    }

    private suspend fun observeMessages(messages: Flow<List<Message>>) {
        messages.collectLatest { latestMessages ->
            update(
                state.value.copy(
                    latestMessages = latestMessages,
                )
            )
        }
    }

    private suspend fun observeChats(chats: Flow<List<Chat>>) {
        chats.collectLatest { latestChats ->
            update(
                state.value.copy(
                    chats = latestChats,
                )
            )
        }
    }

    fun onBackPressed() {
        navHostController.popBackStack()
    }

    fun onChatRequestListClick() {
        navHostController.navigate(LensaScreens.CHAT_REQUEST_LIST_SCREEN.name)
    }

    fun onAddGroupClick() {
        TODO("Not yet implemented")
    }

    fun onChatClick(chatId: String) {
        navHostController.navigate(
            LensaScreens.CHAT_SCREEN.name + "/$chatId"
        )
    }

    fun onEditChatClick() {
        // TODO open editor
    }

    fun onDeleteChatClick(chatId: String) {
        viewModelScope.launch {
            chatRepository.deleteChat(chatId)
        }
    }
}