package dam2.jetpack.proyectofinal.chat.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import dam2.jetpack.proyectofinal.chat.domain.model.ChatMessage
import dam2.jetpack.proyectofinal.chat.domain.repository.ChatRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Implementación del [ChatRepository] que utiliza Firebase Firestore como backend.
 *
 * Se encarga de la lógica para enviar y recibir mensajes en tiempo real para un chat
 * específico asociado a un evento y dos usuarios.
 *
 * @property db Instancia de [FirebaseFirestore] inyectada por Hilt.
 */
class ChatRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : ChatRepository {

    /**
     * Genera un identificador de chat único y predecible para un evento y dos usuarios.
     *
     * El ID se construye combinando el ID del evento y los UIDs de los dos usuarios.
     * Los UIDs se ordenan alfabéticamente para asegurar que el ID sea el mismo
     * independientemente de qué usuario inicie la conversación.
     *
     * @param eventId El ID del evento al que pertenece el chat.
     * @param uid1 El UID del primer usuario.
     * @param uid2 El UID del segundo usuario.
     * @return Una cadena de texto con el formato "eventId_uidOrdenado1_uidOrdenado2".
     */
    private fun chatId(eventId: String, uid1: String, uid2: String): String =
        eventId + "_" + listOf(uid1, uid2).sorted().joinToString("_")

    /**
     * Escucha en tiempo real los mensajes de un chat específico.
     *
     * Devuelve un [Flow] que emite la lista completa de mensajes cada vez que hay un cambio
     * en la colección de mensajes del chat en Firestore. Los mensajes se ordenan por
     * fecha de forma descendente.
     *
     * @param eventId El ID del evento.
     * @param otherUid El UID del otro participante en el chat.
     * @return Un Flow que emite `List<ChatMessage>`.
     */
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
                    if (error != null) {
                        // Se puede manejar el error aquí si es necesario, por ejemplo, cerrando el flow.
                        return@addSnapshotListener
                    }
                    trySend(snapshot?.toObjects(ChatMessage::class.java) ?: emptyList())
                }

        // Se asegura de que el listener se cancele cuando el Flow se cierre.
        awaitClose { registration.remove() }
    }

    /**
     * Envía un mensaje de texto a un chat específico.
     *
     * Esta función primero se asegura de que el documento del chat principal exista,
     * almacenando los miembros y el ID del evento. Luego, añade el nuevo mensaje
     * a la subcolección "messages".
     *
     * @param eventId El ID del evento.
     * @param text El contenido del mensaje de texto.
     * @param otherUid El UID del destinatario del mensaje.
     */
    override suspend fun sendMessage(eventId: String, text: String, otherUid: String) {
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val cid = chatId(eventId, currentUid, otherUid)

        val chatRef = db.collection("chats").document(cid)

        // Crea o actualiza el documento del chat con los miembros y el ID del evento.
        // SetOptions.merge() asegura que no se sobrescriban otros campos si existieran.
        chatRef.set(
            mapOf(
                "members" to listOf(currentUid, otherUid),
                "eventId" to eventId
            ),
            SetOptions.merge()
        ).await()

        // Prepara los datos del mensaje.
        val data = mapOf(
            "senderId" to currentUid,
            "text" to text,
            "timestamp" to FieldValue.serverTimestamp() // Usa la marca de tiempo del servidor de Firebase.
        )

        // Añade el nuevo mensaje a la subcolección "messages".
        chatRef.collection("messages")
            .add(data)
            .await()
    }
}
