package ru.arinae_va.lensa.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.arinae_va.lensa.domain.model.chat.Chat

interface IChatRepository {
    suspend fun isChatExist(chatId: String): Boolean
    fun getChats(profileId: String): Flow<List<Chat>>
    fun getChat(chatId: String): Flow<Chat>
    suspend fun upsertChat(chat: Chat)
    suspend fun deleteChat(chatId: String)
}