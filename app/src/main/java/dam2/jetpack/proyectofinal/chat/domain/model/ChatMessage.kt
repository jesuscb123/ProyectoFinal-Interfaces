package dam2.jetpack.proyectofinal.chat.domain.model

import com.google.firebase.Timestamp

data class ChatMessage(
    val senderId: String = "",
    val text: String = "",
    val timestamp: Timestamp = Timestamp.now()
)