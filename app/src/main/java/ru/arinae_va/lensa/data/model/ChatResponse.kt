package ru.arinae_va.lensa.data.model

import ru.arinae_va.lensa.domain.model.chat.Chat
import ru.arinae_va.lensa.domain.model.chat.DialogData
import ru.arinae_va.lensa.utils.parseIsoDatetime

data class ChatResponse(
    val chatId: String? = null,
    val creatorProfileId: String? = null,
    val members: List<String>? = null,
    val name: String? = null,
    val avatarUrl: String? = null,
    val createTime: String? = null,
    val dialogData: DialogDataResponse? = null,
    val pinnedMessageId: String? = null,
) {
    fun toChat() = Chat(
        chatId = chatId.orEmpty(),
        creatorProfileId = creatorProfileId.orEmpty(),
        members = members ?: emptyList(),
        avatarUrl = avatarUrl.orEmpty(),
        name = name.orEmpty(),
        createTime = parseIsoDatetime(createTime.orEmpty()),
        dialogData = dialogData?.toDialogData(),
        pinnedMessageId = pinnedMessageId,
    )
}

fun Chat.toChatResponse() = ChatResponse(
    chatId = chatId,
    creatorProfileId = creatorProfileId,
    members = members,
    name = name,
    avatarUrl = avatarUrl,
    createTime = createTime.toString(),
    dialogData = dialogData.toDialogDataResponse(),
    pinnedMessageId = pinnedMessageId,
)

data class DialogDataResponse(
    val authorMemberName: String? = null,
    val targetMemberName: String? = null,
    val authorAvatarUrl: String? = null,
    val targetAvatarUrl: String? = null,
    val authorSpecialization: String? = null,
    val targetSpecialization: String? = null,
) {
    fun toDialogData() = DialogData(
        authorMemberName = authorMemberName.orEmpty(),
        authorAvatarUrl = authorAvatarUrl,
        authorSpecialization = authorSpecialization.orEmpty(),
        targetAvatarUrl = targetAvatarUrl,
        targetMemberName = targetMemberName.orEmpty(),
        targetSpecialization = targetSpecialization.orEmpty(),
    )
}

private fun DialogData?.toDialogDataResponse() = DialogDataResponse(
    targetMemberName = this?.targetMemberName,
    targetAvatarUrl = this?.targetAvatarUrl,
    targetSpecialization = this?.targetSpecialization,
    authorAvatarUrl = this?.authorAvatarUrl,
    authorMemberName = this?.authorMemberName,
    authorSpecialization = this?.authorSpecialization,
)
