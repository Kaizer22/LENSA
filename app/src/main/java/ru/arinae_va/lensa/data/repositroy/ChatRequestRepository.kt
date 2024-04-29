package ru.arinae_va.lensa.data.repositroy

import kotlinx.coroutines.flow.Flow
import ru.arinae_va.lensa.data.datasource.remote.IChatsDataSource
import ru.arinae_va.lensa.domain.model.ChatRequest
import ru.arinae_va.lensa.domain.repository.IChatRequestRepository
import ru.arinae_va.lensa.domain.repository.IUserProfileRepository
import java.time.LocalDateTime
import javax.inject.Inject

class ChatRequestRepository @Inject constructor(
    private val chatsDataSource: IChatsDataSource,
    private val userProfileRepository: IUserProfileRepository,
): IChatRequestRepository {
    override fun getChatRequests(profileId: String): Flow<List<ChatRequest>> =
        chatsDataSource.getChatRequests(profileId)

    override suspend fun sendChatRequest(
        targetProfileId: String,
        targetProfileName: String,
        targetProfileAvatarUrl: String?,
    ) {
        userProfileRepository.currentUserProfile()?.let { currentUserProfile ->
            val chatRequest = ChatRequest(
                requestId = currentUserProfile.profileId + "_" + targetProfileId,
                authorProfileId = currentUserProfile.profileId,
                targetProfileId = targetProfileId,
                targetName = targetProfileName,
                targetAvatarUrl = targetProfileAvatarUrl,
                dateTime = LocalDateTime.now(),
                authorAvatarUrl = currentUserProfile.avatarUrl,
                authorName = currentUserProfile.name + " " + currentUserProfile.surname,
            )
            chatsDataSource.sendChatRequest(chatRequest)
        }
    }

    override suspend fun approveChatRequest(chatRequest: ChatRequest) {
        chatsDataSource.approveChatRequest(chatRequest)
    }

    override suspend fun cancelChatRequest(chatRequest: ChatRequest) {
        chatsDataSource.cancelChatRequest(chatRequest)
    }
}