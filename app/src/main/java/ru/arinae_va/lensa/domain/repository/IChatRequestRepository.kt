package ru.arinae_va.lensa.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.arinae_va.lensa.domain.model.ChatRequest

interface IChatRequestRepository {
    fun getChatRequests(profileId: String): Flow<List<ChatRequest>>
    suspend fun sendChatRequest(targetProfileId: String)
    suspend fun approveChatRequest(chatRequest: ChatRequest)
    suspend fun cancelChatRequest(chatRequest: ChatRequest)
}