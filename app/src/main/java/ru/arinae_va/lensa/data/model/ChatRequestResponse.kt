package ru.arinae_va.lensa.data.model

import ru.arinae_va.lensa.domain.model.ChatRequest
import ru.arinae_va.lensa.utils.parseIsoDatetime

data class ChatRequestResponse(
    val requestId: String?,
    val authorProfileId: String?,
    val targetProfileId: String?,
    val dateTime: String?,
) {
    fun toChatRequest() = ChatRequest(
        requestId = requestId.orEmpty(),
        authorProfileId = authorProfileId.orEmpty(),
        targetProfileId = targetProfileId.orEmpty(),
        dateTime = parseIsoDatetime(dateTime.orEmpty()),
    )
}

fun ChatRequest.toChatRequestResponse() = ChatRequestResponse(
    requestId = requestId,
    authorProfileId = authorProfileId,
    targetProfileId = targetProfileId,
    dateTime = dateTime.toString(),
)