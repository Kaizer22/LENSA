package ru.arinae_va.lensa.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.arinae_va.lensa.domain.model.user.Presence

interface IPresenceRepository {
    suspend fun setOnline()
    suspend fun setOffline()

    fun getPresence(profileIds: List<String>): Flow<List<Presence>>
}