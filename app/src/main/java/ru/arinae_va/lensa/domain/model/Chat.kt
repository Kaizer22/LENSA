package ru.arinae_va.lensa.domain.model

import java.time.LocalDateTime

data class Chat(
    val chatId: String,
    val creatorProfileId: String,
    val members: List<String>,
    val name: String,
    val avatarUrl: String,
    val createTime: LocalDateTime,
    val dialogData: DialogData? = null,
) {
    fun isDialog() = dialogData != null
    fun getAvatarUrl(currentProfileId: String) = if (isDialog()) {
        if (currentProfileId == creatorProfileId) dialogData?.targetAvatarUrl.orEmpty()
        else dialogData?.authorAvatarUrl.orEmpty()
    } else avatarUrl

    fun getChatName(currentProfileId: String) = if (isDialog()) {
        if (currentProfileId == creatorProfileId) dialogData?.targetMemberName.orEmpty()
        else dialogData?.authorMemberName.orEmpty()
    } else name

    fun getSpecailization(currentProfileId: String) = if (isDialog()) {
        if (currentProfileId == creatorProfileId) dialogData?.targetSpecialization.orEmpty()
        else dialogData?.authorSpecialization.orEmpty()
    } else null
}

data class DialogData(
    val authorMemberName: String,
    val targetMemberName: String,
    val authorAvatarUrl: String?,
    val targetAvatarUrl: String?,
    val authorSpecialization: String?,
    val targetSpecialization: String?,
)
