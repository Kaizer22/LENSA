package ru.arinae_va.lensa.data.model

import ru.arinae_va.lensa.domain.model.Message
import ru.arinae_va.lensa.utils.parseIsoDatetime

data class MessageResponse(
    val messageId: String? = null,
    val authorProfileId: String? = null,
    val isRead: Boolean? = null,
    val chatId: String? = null,
    val message: String? = null,
    val dateTime: String? = null,
) {
    fun toMessage() = Message(
        messageId = messageId.orEmpty(),
        authorProfileId = authorProfileId.orEmpty(),
        chatId = chatId.orEmpty(),
        message = message.orEmpty(),
        dateTime = parseIsoDatetime(dateTime.orEmpty()),
        isRead = isRead ?: false,
    )
}

fun Message.toMessageResponse() = MessageResponse(
    messageId = messageId,
    authorProfileId = authorProfileId,
    chatId = chatId,
    message = message,
    dateTime = dateTime.toString(),
)