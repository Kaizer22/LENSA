package ru.arinae_va.lensa.data.datasource.remote

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import ru.arinae_va.lensa.data.model.PresenceResponse
import ru.arinae_va.lensa.domain.model.user.Presence
import java.time.LocalDateTime
import javax.inject.Inject


interface IPresenceDataSource {
    suspend fun setOnline(profileId: String)
    suspend fun setOffline(profileId: String)
    fun getPresence(profileIds: List<String>): Flow<List<Presence>>
}

private const val USER_PRESENCE_COLLECTION = "presence"

private const val PROFILE_ID_FIELD = "profileId"

class FirebasePresenceDataSource @Inject constructor(
    database: FirebaseFirestore,
): IPresenceDataSource {

    private val presence = database.collection(USER_PRESENCE_COLLECTION)

    private val coroutineContext = Dispatchers.IO + Job()
    private val coroutineScope = CoroutineScope(coroutineContext)
    override suspend fun setOnline(profileId: String) {
        val now = LocalDateTime.now()
        val presenceData = PresenceResponse(
            profileId = profileId,
            online = true,
            lastOnline = now.toString()
        )
        presence.document(profileId)
            .set(presenceData)
            .await()
    }

    override suspend fun setOffline(profileId: String) {
        val now = LocalDateTime.now()
        val presenceData = PresenceResponse(
            profileId = profileId,
            online = false,
            lastOnline = now.toString()
        )
        presence.document(profileId)
            .set(presenceData)
            .await()
    }

    override fun getPresence(profileIds: List<String>) = callbackFlow {
        val listener = presence.whereIn(PROFILE_ID_FIELD, profileIds)
            .addSnapshotListener { value, error ->
                error?.let { close(error) }
                val presenceSnapshot = value?.documents
                    ?.mapNotNull { it.toObject(PresenceResponse::class.java) }
                    ?.map { it.toPresence() }
                presenceSnapshot?.let {
                    coroutineScope.launch {
                        send(presenceSnapshot)
                    }
                }
            }
        awaitClose {
            listener.remove()
        }
    }
}