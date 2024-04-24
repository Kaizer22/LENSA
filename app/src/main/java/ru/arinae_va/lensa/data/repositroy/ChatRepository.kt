package ru.arinae_va.lensa.data.repositroy

import ru.arinae_va.lensa.data.datasource.remote.IChatsDataSource
import ru.arinae_va.lensa.domain.model.Chat
import ru.arinae_va.lensa.domain.repository.IChatRepository
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val chatsDataSource: IChatsDataSource,
) : IChatRepository {
    override fun getChats(profileId: String) = chatsDataSource.getChats(profileId)

    override suspend fun upsertChat(chat: Chat) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteChat(chatId: String) {
        TODO("Not yet implemented")
    }
}