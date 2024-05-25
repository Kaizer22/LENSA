package ru.arinae_va.lensa.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.arinae_va.lensa.domain.model.chat.Message

interface IMessageRepository {
    fun getLastMessages(chatIds: List<String>): Flow<List<Message>>
    fun getMessages(chatId: String): Flow<List<Message>>

    suspend fun pinMessage(chatId:String, messageId:String)
    suspend fun setMessagesRead(messages: List<Message>)
    suspend fun upsertMessage(message: Message)
    suspend fun deleteMessage(messageId: String)
}