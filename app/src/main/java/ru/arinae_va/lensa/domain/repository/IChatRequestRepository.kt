package ru.arinae_va.lensa.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.arinae_va.lensa.domain.model.ChatRequest

interface IChatRequestRepository {
    fun getChatRequests(profileId: String): Flow<List<ChatRequest>>
    // TODO refactor arguments
    suspend fun sendChatRequest(
        targetProfileId: String,
        targetProfileName: String,
        targetProfileSpecialization: String,
        targetProfileAvatarUrl: String?,
    )
    suspend fun approveChatRequest(chatRequest: ChatRequest)
    suspend fun cancelChatRequest(chatRequest: ChatRequest)
}