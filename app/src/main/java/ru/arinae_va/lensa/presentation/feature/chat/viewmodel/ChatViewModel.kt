package ru.arinae_va.lensa.presentation.feature.chat.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.arinae_va.lensa.domain.model.chat.Chat
import ru.arinae_va.lensa.domain.model.chat.Message
import ru.arinae_va.lensa.domain.model.user.Presence
import ru.arinae_va.lensa.domain.repository.IChatRepository
import ru.arinae_va.lensa.domain.repository.IMessageRepository
import ru.arinae_va.lensa.domain.repository.IPresenceRepository
import ru.arinae_va.lensa.domain.repository.IUserProfileRepository
import ru.arinae_va.lensa.presentation.common.StateViewModel
import ru.arinae_va.lensa.presentation.navigation.LensaScreens
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val navHostController: NavHostController,
    private val chatRepository: IChatRepository,
    private val messageRepository: IMessageRepository,
    private val presenceRepository: IPresenceRepository,
    userProfileRepository: IUserProfileRepository,
) : StateViewModel<ChatScreenState>(
    initialState = ChatScreenState(
        currentProfileId = userProfileRepository.currentProfileId().orEmpty(),
        isLoading = true,
        chat = null,
        interlocutorPresence = null,
        messages = emptyList(),
        messageInput = "",
    )
) {
    private var presenceJob: Job? = null
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
            updateSuspending(
                state.value.copy(
                    messages = latestMessages,
                )
            )
            if (state.value.messages.any {
                    it.authorProfileId != state.value.currentProfileId && !it.isRead
                }) {
                messageRepository.setMessagesRead(latestMessages)
            }
        }
    }

    private suspend fun observeCurrentChat(chat: Flow<Chat>) {
        chat.collectLatest { newChat ->
            presenceJob?.cancelChildren()
            presenceJob?.cancel()
            updateSuspending(
                state.value.copy(
                    chat = newChat,
                )
            )

            if ((state.value.chat?.members?.size ?: 0) > 1) {
                presenceJob = viewModelScope.launch(Dispatchers.IO) {
                    val presence = presenceRepository.getPresence(state.value.chat?.members!!)
                    observePresence(presence)
                }
            }
        }
    }

    private suspend fun observePresence(presence: Flow<List<Presence>>) {
        presence.collectLatest { membersLatestPresence ->
            update(
                state.value.copy(
                    interlocutorPresence = membersLatestPresence.firstOrNull {
                        it.profileId != state.value.currentProfileId
                    }
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
                        isRead = false,
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

    // If not a group
    fun onAvatarClick() {
        val isSelf = false
        state.value.chat?.members?.find { it != state.value.currentProfileId }?.let { receiverId ->
            navHostController.navigate(
                "${LensaScreens.SPECIALIST_DETAILS_SCREEN.name}/" +
                        "$receiverId/" +
                        "$isSelf"
            )
        }

    }
}