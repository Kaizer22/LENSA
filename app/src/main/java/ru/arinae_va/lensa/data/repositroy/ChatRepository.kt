package ru.arinae_va.lensa.data.repositroy

import kotlinx.coroutines.flow.Flow
import ru.arinae_va.lensa.data.datasource.remote.IChatsDataSource
import ru.arinae_va.lensa.domain.model.Chat
import ru.arinae_va.lensa.domain.repository.IChatRepository
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val chatsDataSource: IChatsDataSource,
) : IChatRepository {
    override fun getChats(profileId: String) = chatsDataSource.getChats(profileId)

    override fun getChat(chatId: String): Flow<Chat> = chatsDataSource.getChat(chatId)

    override suspend fun upsertChat(chat: Chat) {
        chatsDataSource.upsertChat(chat)
    }

    override suspend fun deleteChat(chatId: String) {
        chatsDataSource.deleteChat(chatId)
    }
}