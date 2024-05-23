package ru.arinae_va.lensa.data.model

import ru.arinae_va.lensa.domain.model.user.Presence
import ru.arinae_va.lensa.utils.parseIsoDatetime

data class PresenceResponse(
    val profileId: String? = null,
    val online: Boolean? = null,
    val lastOnline: String? = null,
) {
    fun toPresence() = Presence(
        profileId = profileId.orEmpty(),
        isOnline = online ?: false,
        lastOnline = parseIsoDatetime(lastOnline.orEmpty()),
    )
}

fun Presence.toPresenceResponse() = PresenceResponse(
    profileId = profileId,
    online = isOnline,
    lastOnline = lastOnline.toString()
)