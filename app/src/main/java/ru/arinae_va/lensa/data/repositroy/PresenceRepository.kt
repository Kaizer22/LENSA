package ru.arinae_va.lensa.data.repositroy

import kotlinx.coroutines.flow.Flow
import ru.arinae_va.lensa.data.datasource.remote.IPresenceDataSource
import ru.arinae_va.lensa.domain.model.user.Presence
import ru.arinae_va.lensa.domain.repository.IPresenceRepository
import ru.arinae_va.lensa.domain.repository.IUserProfileRepository
import javax.inject.Inject

class PresenceRepository @Inject constructor(
    private val presenceDataSource: IPresenceDataSource,
    private val userProfileRepository: IUserProfileRepository,
) : IPresenceRepository {
    override suspend fun setOnline() = userProfileRepository.currentProfileId()?.let {
        presenceDataSource.setOnline(it)
    } ?: Unit

    override suspend fun setOffline() = userProfileRepository.currentProfileId()?.let {
        presenceDataSource.setOffline(it)
    } ?: Unit

    override fun getPresence(profileIds: List<String>): Flow<List<Presence>> =
        presenceDataSource.getPresence(profileIds)
}