package dam2.jetpack.proyectofinal.chat.domain.repository

import dam2.jetpack.proyectofinal.chat.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun listenMessages(eventId: String, otherUid: String): Flow<List<ChatMessage>>
    suspend fun sendMessage(eventId: String, text: String, otherUid: String)
}