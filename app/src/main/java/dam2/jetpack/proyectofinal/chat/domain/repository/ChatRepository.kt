package dam2.jetpack.proyectofinal.chat.domain.repository

import dam2.jetpack.proyectofinal.chat.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun listenMessages(otherUid: String): Flow<List<ChatMessage>>
    suspend fun sendMessage(text: String, otherUid: String)
}