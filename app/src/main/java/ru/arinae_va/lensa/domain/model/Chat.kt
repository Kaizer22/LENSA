package ru.arinae_va.lensa.domain.model

import ru.arinae_va.lensa.data.model.ChatResponse
import java.time.LocalDateTime

data class Chat (
    val chatId: String,
    val creatorProfileId: String,
    val members: List<String>,
    val name: String,
    val avatarUrl: String,
    val createTime: LocalDateTime,
)

fun Chat.toChatResponse() = ChatResponse(
    chatId = chatId,
    creatorProfileId = creatorProfileId,
    members = members,
    name = name,
    avatarUrl = avatarUrl,
    createTime = createTime.toString(),
)