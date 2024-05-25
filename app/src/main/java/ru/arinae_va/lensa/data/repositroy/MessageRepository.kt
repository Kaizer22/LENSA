package ru.arinae_va.lensa.data.repositroy

import kotlinx.coroutines.flow.Flow
import ru.arinae_va.lensa.data.datasource.remote.IChatsDataSource
import ru.arinae_va.lensa.domain.model.chat.Message
import ru.arinae_va.lensa.domain.repository.IMessageRepository
import javax.inject.Inject

class MessageRepository @Inject constructor(
    private val chatsDataSource: IChatsDataSource,
): IMessageRepository {
    override suspend fun upsertMessage(message: Message) {
        chatsDataSource.upsertMessage(message)
    }

    override suspend fun deleteMessage(messageId: String) {
        chatsDataSource.deleteMessage(messageId)
    }

    override fun getLastMessages(chatIds: List<String>): Flow<List<Message>> =
        chatsDataSource.getLastMessages(chatIds)

    override fun getMessages(chatId: String) = chatsDataSource.getMessages(chatId)
    override suspend fun pinMessage(chatId: String, messageId: String) {
        chatsDataSource.pinMessage(chatId, messageId)
    }

    override suspend fun setMessagesRead(messages: List<Message>) {
        chatsDataSource.setMessagesRead(messages)
    }
}