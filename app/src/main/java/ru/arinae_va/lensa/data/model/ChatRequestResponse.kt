package ru.arinae_va.lensa.data.model

import ru.arinae_va.lensa.domain.model.ChatRequest
import ru.arinae_va.lensa.utils.parseIsoDatetime

data class ChatRequestResponse(
    val requestId: String? = null,
    val authorAvatarUrl: String? = null,
    val targetAvatarUrl: String? = null,
    val authorName: String? = null,
    val targetName: String? = null,
    val targetSpecialization: String? = null,
    val authorSpecialization: String? = null,
    val authorProfileId: String? = null,
    val targetProfileId: String? = null,
    val dateTime: String? = null,
) {
    fun toChatRequest() = ChatRequest(
        requestId = requestId.orEmpty(),
        authorName = authorName.orEmpty(),
        targetName = targetName.orEmpty(),
        authorAvatarUrl = authorAvatarUrl,
        targetAvatarUrl = targetAvatarUrl,
        authorSpecialization = authorSpecialization.orEmpty(),
        targetSpecialization = targetSpecialization.orEmpty(),
        authorProfileId = authorProfileId.orEmpty(),
        targetProfileId = targetProfileId.orEmpty(),
        dateTime = parseIsoDatetime(dateTime.orEmpty()),
    )
}

fun ChatRequest.toChatRequestResponse() = ChatRequestResponse(
    requestId = requestId,
    authorName = authorName,
    authorAvatarUrl = authorAvatarUrl,
    authorProfileId = authorProfileId,
    targetProfileId = targetProfileId,
    targetAvatarUrl = targetAvatarUrl,
    targetName = targetName,
    dateTime = dateTime.toString(),
)