package dam2.jetpack.proyectofinal.chat.domain.repository

import dam2.jetpack.proyectofinal.chat.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz que define las operaciones para el repositorio de chat.
 *
 * Esta abstracción permite desacoplar la lógica de negocio de la implementación
 * específica de la fuente de datos (por ejemplo, Firebase Firestore, una API REST, etc.).
 */
interface ChatRepository {
    /**
     * Escucha los mensajes de un chat específico en tiempo real.
     *
     * @param eventId El ID del evento al que está asociado el chat.
     * @param otherUid El ID del otro usuario en la conversación.
     * @return Un [Flow] que emite una lista de [ChatMessage] cada vez que hay una actualización.
     */
    fun listenMessages(eventId: String, otherUid: String): Flow<List<ChatMessage>>

    /**
     * Envía un nuevo mensaje al chat.
     *
     * @param eventId El ID del evento al que está asociado el chat.
     * @param text El contenido del mensaje a enviar.
     * @param otherUid El ID del otro usuario en la conversación.
     */
    suspend fun sendMessage(eventId: String, text: String, otherUid: String)
}
