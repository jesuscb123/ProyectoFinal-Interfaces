package dam2.jetpack.proyectofinal.chat.data

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import dam2.jetpack.proyectofinal.chat.domain.model.ChatMessage
import dam2.jetpack.proyectofinal.chat.domain.repository.ChatRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : ChatRepository {

    private fun chatId(eventId: String, uid1: String, uid2: String): String =
        eventId + "_" + listOf(uid1, uid2).sorted().joinToString("_")


    override fun listenMessages(eventId: String, otherUid: String): Flow<List<ChatMessage>> = callbackFlow {
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUid == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val cid = chatId(eventId, currentUid, otherUid)

        val registration =
            db.collection("chats")
                .document(cid)
                .collection("messages")
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) return@addSnapshotListener
                    trySend(snapshot?.toObjects(ChatMessage::class.java) ?: emptyList())
                }

        awaitClose { registration.remove() }
    }

    override suspend fun sendMessage(eventId: String, text: String, otherUid: String) {
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val cid = chatId(eventId, currentUid, otherUid)

        val chatRef = db.collection("chats").document(cid)

        // Crea/actualiza doc chat con miembros
        chatRef.set(
            mapOf(
                "members" to listOf(currentUid, otherUid),
                "eventId" to eventId
            ),
            SetOptions.merge()
        ).await()

        val msg = ChatMessage(
            senderId = currentUid,
            text = text,
            timestamp = Timestamp.now()
        )

        chatRef.collection("messages").add(msg).await()
    }


}
