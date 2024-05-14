package ru.arinae_va.lensa.data.datasource.remote.legacy

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import ru.arinae_va.lensa.data.model.ChatRequestResponse
import ru.arinae_va.lensa.data.model.toChatRequestResponse
import ru.arinae_va.lensa.domain.model.chat.Chat
import ru.arinae_va.lensa.domain.model.legacy.ChatRequest
import ru.arinae_va.lensa.domain.model.chat.DialogData
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

interface IChatRequestsDataSource {
    fun getChatRequests(profileId: String): Flow<List<ChatRequest>>
    suspend fun sendChatRequest(chatRequest: ChatRequest)
    suspend fun approveChatRequest(chatRequest: ChatRequest)
    suspend fun cancelChatRequest(chatRequest: ChatRequest)
}

private const val CHAT_REQUEST_COLLECTION = "chat_request"

private const val TARGET_PROFILE_ID_FIELD = "targetProfileId"

class FirebaseChatRequestsDataSource @Inject constructor(
    database: FirebaseFirestore,
) : IChatRequestsDataSource {

    private val coroutineContext = Dispatchers.IO + Job()
    private val coroutineScope = CoroutineScope(coroutineContext)

    private val chatRequests = database.collection(CHAT_REQUEST_COLLECTION)
    override fun getChatRequests(profileId: String): Flow<List<ChatRequest>> = callbackFlow {
        val listener = chatRequests.whereEqualTo(TARGET_PROFILE_ID_FIELD, profileId)
            .addSnapshotListener { value, error ->
                error?.let { close(error) }
                val chatRequestsSnapshot = value?.documents
                    ?.mapNotNull { it.toObject(ChatRequestResponse::class.java) }
                    ?.map { it.toChatRequest() }
                chatRequestsSnapshot?.let {
                    coroutineScope.launch {
                        send(chatRequestsSnapshot)
                    }
                }
            }
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun sendChatRequest(chatRequest: ChatRequest) {
        val mapped = chatRequest.toChatRequestResponse()
        chatRequests.document(chatRequest.requestId)
            .set(mapped)
            .await()
    }

    override suspend fun approveChatRequest(chatRequest: ChatRequest) {
        val chat = Chat(
            chatId = UUID.randomUUID().toString(),
            creatorProfileId = chatRequest.authorProfileId,
            members = listOf(
                chatRequest.authorProfileId,
                chatRequest.targetProfileId,
            ),
            name = "",
            avatarUrl = "",
            createTime = LocalDateTime.now(),
            dialogData = DialogData(
                authorMemberName = chatRequest.authorName,
                authorAvatarUrl = chatRequest.authorAvatarUrl,
                targetMemberName = chatRequest.targetName,
                targetAvatarUrl = chatRequest.targetAvatarUrl,
                targetSpecialization = chatRequest.targetSpecialization,
                authorSpecialization = chatRequest.authorSpecialization,
            )
        )
        //upsertChat(chat)
        cancelChatRequest(chatRequest)
    }

    override suspend fun cancelChatRequest(chatRequest: ChatRequest) {
        chatRequests.document(chatRequest.requestId)
            .delete()
            .await()
    }
}