package ru.arinae_va.lensa.data.repositroy

import ru.arinae_va.lensa.data.datasource.remote.IChatsDataSource
import ru.arinae_va.lensa.domain.model.Message
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

    override fun getMessages(chatId: String) = chatsDataSource.getMessages(chatId)
}