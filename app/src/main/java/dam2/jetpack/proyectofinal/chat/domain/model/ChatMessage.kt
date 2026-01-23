package dam2.jetpack.proyectofinal.chat.domain.model

import com.google.firebase.Timestamp

/**
 * Modelo de datos que representa un único mensaje dentro de un chat.
 *
 * Esta clase de datos es utilizada por Firestore para serializar y deserializar
 * los documentos de mensajes. Los valores por defecto vacíos son necesarios para
 * que Firestore pueda instanciar el objeto correctamente.
 *
 * @property senderId El ID del usuario que envió el mensaje.
 * @property text El contenido de texto del mensaje.
 * @property timestamp La marca de tiempo de cuándo se envió el mensaje, proporcionada por Firebase.
 */
data class ChatMessage(
    val senderId: String = "",
    val text: String = "",
    val timestamp: Timestamp = Timestamp.now()
)
