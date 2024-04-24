package ru.arinae_va.lensa.data.model

import ru.arinae_va.lensa.domain.model.Message
import ru.arinae_va.lensa.utils.parseIsoDatetime

data class MessageResponse(
    val messageId: String?,
    val authorProfileId: String?,
    val chatId: String?,
    val message: String?,
    val dateTime: String?,
) {
    fun toMessage() = Message(
        messageId = messageId.orEmpty(),
        authorProfileId = authorProfileId.orEmpty(),
        chatId = chatId.orEmpty(),
        message = message.orEmpty(),
        dateTime = parseIsoDatetime(dateTime.orEmpty()),
    )
}

fun Message.toMessageResponse() = MessageResponse(
    messageId = messageId,
    authorProfileId = authorProfileId,
    chatId = chatId,
    message = message,
    dateTime = dateTime.toString(),
)