package ru.arinae_va.lensa.data.model

import ru.arinae_va.lensa.domain.model.Chat
import ru.arinae_va.lensa.utils.parseIsoDatetime

data class ChatResponse(
    val chatId: String?,
    val creatorProfileId: String?,
    val members: List<String>?,
    val name: String?,
    val avatarUrl: String?,
    val createTime: String?,
) {
    fun toChat() = Chat(
        chatId = chatId.orEmpty(),
        creatorProfileId = creatorProfileId.orEmpty(),
        members = members ?: emptyList(),
        avatarUrl = avatarUrl.orEmpty(),
        name = name.orEmpty(),
        createTime = parseIsoDatetime(createTime.orEmpty())
    )
}