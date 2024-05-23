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
import ru.arinae_va.lensa.data.model.ChatResponse
import ru.arinae_va.lensa.data.model.MessageResponse
import ru.arinae_va.lensa.data.model.toChatResponse
import ru.arinae_va.lensa.data.model.toMessageResponse
import ru.arinae_va.lensa.domain.model.chat.Chat
import ru.arinae_va.lensa.domain.model.chat.Message
import javax.inject.Inject

interface IChatsDataSource {

    fun getMessages(chatId: String): Flow<List<Message>>
    fun getLastMessages(chatIds: List<String>): Flow<List<Message>>

    suspend fun setMessagesRead(messagesToRead: List<Message>)
    suspend fun upsertMessage(message: Message)
    suspend fun deleteMessage(messageId: String)

    fun getChats(profileId: String): Flow<List<Chat>>
    suspend fun upsertChat(chat: Chat)
    suspend fun deleteChat(chatId: String)
    fun getChat(chatId: String): Flow<Chat>
    suspend fun isChatExist(chatId: String): Boolean

}

private const val MESSAGE_COLLECTION = "message"
private const val CHAT_COLLECTION = "chat"
private const val BLACK_LIST_COLLECTION = "blackList"

private const val CHAT_ID_FIELD = "chatId"
private const val MESSAGE_ID_FIELD = "messageId"
private const val MEMBERS_FIELD = "members"

class FirebaseChatsDataSource @Inject constructor(
    private val database: FirebaseFirestore,
) : IChatsDataSource {
    private val chats = database.collection(CHAT_COLLECTION)
    private val messages = database.collection(MESSAGE_COLLECTION)

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

    override fun getLastMessages(chatIds: List<String>) = callbackFlow {
        val listener = messages.whereIn(CHAT_ID_FIELD, chatIds)
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

    override suspend fun setMessagesRead(messagesToRead: List<Message>) {
        val readMessages = messagesToRead.filter { !it.isRead }
            .map { it.copy(isRead = true) }
        database.runBatch { batch ->
            readMessages.forEach { message ->
                val ref = messages.document(message.messageId)
                batch.set(ref, message.toMessageResponse())
            }
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
        // TODO delete messages too
        chats.document(chatId)
            .delete()
            .await()
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

    override suspend fun isChatExist(chatId: String): Boolean =
        chats.whereEqualTo(CHAT_ID_FIELD, chatId)
            .get()
            .await()
            .documents.size > 0
}