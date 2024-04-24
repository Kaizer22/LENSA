package ru.arinae_va.lensa.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.arinae_va.lensa.domain.model.Message

interface IMessageRepository {
    fun getMessages(chatId: String): Flow<List<Message>>
    suspend fun upsertMessage(message: Message)
    suspend fun deleteMessage(messageId: String)
}