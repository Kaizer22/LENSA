package ru.arinae_va.lensa.domain.model

import java.time.LocalDateTime

data class Chat (
    val chatId: String,
    val creatorProfileId: String,
    val members: List<String>,
    val name: String,
    val avatarUrl: String,
    val createTime: LocalDateTime,
    val dialogData: DialogData? = null,
)

data class DialogData(
    val authorMemberName: String,
    val targetMemberName: String,
    val authorAvatarUrl: String?,
    val targetAvatarUrl: String?,
)
