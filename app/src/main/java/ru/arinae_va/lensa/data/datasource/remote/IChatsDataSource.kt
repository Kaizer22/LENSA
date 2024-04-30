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
import ru.arinae_va.lensa.data.model.ChatRequestResponse
import ru.arinae_va.lensa.data.model.ChatResponse
import ru.arinae_va.lensa.data.model.MessageResponse
import ru.arinae_va.lensa.data.model.toChatRequestResponse
import ru.arinae_va.lensa.data.model.toChatResponse
import ru.arinae_va.lensa.data.model.toMessageResponse
import ru.arinae_va.lensa.domain.model.Chat
import ru.arinae_va.lensa.domain.model.ChatRequest
import ru.arinae_va.lensa.domain.model.DialogData
import ru.arinae_va.lensa.domain.model.Message
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

interface IChatsDataSource {
    fun getMessages(chatId: String): Flow<List<Message>>
    fun getLastMessages(chatIds: List<String>): Flow<List<Message>>
    suspend fun upsertMessage(message: Message)
    suspend fun deleteMessage(messageId: String)

    fun getChats(profileId: String): Flow<List<Chat>>
    suspend fun upsertChat(chat: Chat)
    suspend fun deleteChat(chatId: String)

    fun getChatRequests(profileId: String): Flow<List<ChatRequest>>
    fun getChat(chatId: String): Flow<Chat>
    suspend fun sendChatRequest(chatRequest: ChatRequest)
    suspend fun approveChatRequest(chatRequest: ChatRequest)
    suspend fun cancelChatRequest(chatRequest: ChatRequest)
}

private const val MESSAGE_COLLECTION = "message"
private const val CHAT_COLLECTION = "chat"
private const val CHAT_REQUEST_COLLECTION = "chat_request"

private const val CHAT_ID_FIELD = "chatId"
private const val MEMBERS_FIELD = "members"
private const val TARGET_PROFILE_ID_FIELD = "targetProfileId"

class FirebaseChatsDataSource @Inject constructor(
    database: FirebaseFirestore,
) : IChatsDataSource {
    private val chats = database.collection(CHAT_COLLECTION)
    private val messages = database.collection(MESSAGE_COLLECTION)
    private val chatRequests = database.collection(CHAT_REQUEST_COLLECTION)

    private val coroutineContext = Dispatchers.IO + Job()
    private val coroutineScope = CoroutineScope(coroutineContext)

    override fun getMessages(chatId: String) = callbackFlow {
        val listener = messages.whereEqualTo(CHAT_ID_FIELD, chatId)
            .addSnapshotListener { value, error ->
                error?.let { close(error) }
                val messagesSnapshot = value?.documents
                    ?.mapNotNull { it.toObject(MessageResponse::class.java) }
                    ?.map { it.toMessage() }
                    ?.sortedBy { it.dateTime }
                messagesSnapshot?.let {
                    coroutineScope.launch {
                        send(messagesSnapshot)
                    }
                }
            }
        awaitClose {
            listener.remove()
        }
    }

    override fun getLastMessages(chatIds: List<String>)= callbackFlow {
        val listener = messages.whereArrayContains(CHAT_ID_FIELD, chatIds)
            .addSnapshotListener { value, error ->
                error?.let { close(error) }
                val messagesSnapshot = value?.documents
                    ?.mapNotNull { it.toObject(MessageResponse::class.java) }
                    ?.map { it.toMessage() }
                    ?.groupBy { it.chatId }
                    ?.map {
                        it.value.maxBy { message -> message.dateTime }
                    }
                messagesSnapshot?.let {
                    coroutineScope.launch {
                        send(messagesSnapshot)
                    }
                }
            }
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun upsertMessage(message: Message) {
        val mapped = message.toMessageResponse()
        messages.document(message.messageId)
            .set(mapped)
            .await()
    }

    override suspend fun deleteMessage(messageId: String) {
        messages.document(messageId)
            .delete()
            .await()
    }


    override fun getChats(profileId: String) = callbackFlow {
        val listener = chats.whereArrayContains(MEMBERS_FIELD, profileId)
            .addSnapshotListener { value, error ->
                error?.let { close(error) }
                val chatsSnapshot = value?.documents
                    ?.mapNotNull { it.toObject(ChatResponse::class.java) }
                    ?.map { it.toChat() }
                chatsSnapshot?.let {
                    coroutineScope.launch {
                        send(chatsSnapshot)
                    }
                }
            }
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun upsertChat(chat: Chat) {
        val mapped = chat.toChatResponse()
        chats.document(chat.chatId)
            .set(mapped)
            .await()
    }

    override suspend fun deleteChat(chatId: String) {
        chats.document(chatId)
            .delete()
            .await()
    }

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

    override fun getChat(chatId: String): Flow<Chat> = callbackFlow {
        val listener = chats.whereEqualTo(CHAT_ID_FIELD, chatId)
            .addSnapshotListener { value, error ->
                error?.let { close(error) }
                val chatSnapshot = value?.documents
                    ?.mapNotNull { it.toObject(ChatResponse::class.java) }
                    ?.map { it.toChat() }
                chatSnapshot?.let {
                    coroutineScope.launch {
                        send(chatSnapshot.first())
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
        upsertChat(chat)
        cancelChatRequest(chatRequest)
    }

    override suspend fun cancelChatRequest(chatRequest: ChatRequest) {
        chatRequests.document(chatRequest.requestId)
            .delete()
            .await()
    }
}