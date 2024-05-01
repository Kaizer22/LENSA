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
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val navHostController: NavHostController,
    private val chatRepository: IChatRepository,
    private val messageRepository: IMessageRepository,
    userProfileRepository: IUserProfileRepository,
): StateViewModel<ChatScreenState>(
    initialState = ChatScreenState(
        currentProfileId = userProfileRepository.currentProfileId().orEmpty(),
        isLoading = true,
        chat = null,
        messages = emptyList(),
        messageInput = "",
    )
) {
    fun onAttach(chatId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentChat = chatRepository.getChat(chatId)
            observeCurrentChat(currentChat)
        }
        viewModelScope.launch(Dispatchers.IO) {
            val messages = messageRepository.getMessages(chatId)
            observeLatestMessages(messages)
        }
    }

    private suspend fun observeLatestMessages(messages: Flow<List<Message>>) {
        messages.collectLatest { latestMessages ->
            update(
                state.value.copy(
                    messages = latestMessages,
                )
            )
        }
    }

    private suspend fun observeCurrentChat(chat: Flow<Chat>) {
        chat.collectLatest { newChat ->
            update(
                state.value.copy(
                    chat = newChat,
                )
            )
        }
    }

    fun onEditMessageClick(messageId: String) {
        val message = state.value.messages.first { it.messageId == messageId }
        update(
            state.value.copy(
                messageInput = message.message,
                editingMessageId = messageId
            )
        )
    }

    fun onDeleteMessageClick(messageId: String) {
        viewModelScope.launch {
            messageRepository.deleteMessage(messageId)
        }
    }

    fun onSendMessageClick() {
        viewModelScope.launch {
            if (state.value.currentProfileId.isNotEmpty()) {
                val message = if (!state.value.editingMessageId.isNullOrEmpty()) {
                    state.value.messages.first { it.messageId == state.value.editingMessageId }
                        .copy(
                            message = state.value.messageInput,
                        )
                } else {
                    Message(
                        messageId = UUID.randomUUID().toString(),
                        authorProfileId = state.value.currentProfileId,
                        chatId = state.value.chat?.chatId.orEmpty(),
                        message = state.value.messageInput,
                        dateTime = LocalDateTime.now(),
                    )
                }
                messageRepository.upsertMessage(message)
                update(
                    state.value.copy(
                        messageInput = "",
                        editingMessageId = null,
                    )
                )
            }
        }
    }

    fun onBackPressed() {
        navHostController.popBackStack()
    }

    fun onMessageInputChanged(message: String) {
        update(
            state.value.copy(
                messageInput = message,
            )
        )
    }

    fun onCancelEditing() {
        update(
            state.value.copy(
                messageInput = "",
                editingMessageId = null,
            )
        )
    }
}