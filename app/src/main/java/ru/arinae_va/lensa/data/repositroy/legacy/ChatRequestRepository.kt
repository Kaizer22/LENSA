package ru.arinae_va.lensa.data.repositroy.legacy

import kotlinx.coroutines.flow.Flow
import ru.arinae_va.lensa.data.datasource.remote.legacy.IChatRequestsDataSource
import ru.arinae_va.lensa.domain.model.legacy.ChatRequest
import ru.arinae_va.lensa.domain.repository.legacy.IChatRequestRepository
import ru.arinae_va.lensa.domain.repository.IUserProfileRepository
import java.time.LocalDateTime
import javax.inject.Inject

class ChatRequestRepository @Inject constructor(
    private val chatIChatRequestsDataSource: IChatRequestsDataSource,
    private val userProfileRepository: IUserProfileRepository,
): IChatRequestRepository {
    override fun getChatRequests(profileId: String): Flow<List<ChatRequest>> =
        chatIChatRequestsDataSource.getChatRequests(profileId)

    override suspend fun sendChatRequest(
        targetProfileId: String,
        targetProfileName: String,
        targetProfileSpecialization: String,
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
                authorSpecialization = currentUserProfile.specialization,
                targetSpecialization = targetProfileSpecialization,
            )
            chatIChatRequestsDataSource.sendChatRequest(chatRequest)
        }
    }

    override suspend fun approveChatRequest(chatRequest: ChatRequest) {
        chatIChatRequestsDataSource.approveChatRequest(chatRequest)
    }

    override suspend fun cancelChatRequest(chatRequest: ChatRequest) {
        chatIChatRequestsDataSource.cancelChatRequest(chatRequest)
    }
}