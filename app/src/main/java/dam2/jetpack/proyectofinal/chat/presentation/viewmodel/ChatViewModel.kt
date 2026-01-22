package dam2.jetpack.proyectofinal.chat.presentation.viewmodel

import androidx.compose.foundation.layout.add
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import dam2.jetpack.proyectofinal.chat.domain.model.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages = _messages.asStateFlow()

    private var chatId: String? = null

    fun loadMessages(recipientUid: String) {
        val currentUserUid = auth.currentUser?.uid ?: return

        // Genera un ID de chat único y consistente, ordenando los UIDs alfabéticamente
        chatId = if (currentUserUid < recipientUid) {
            "${currentUserUid}_${recipientUid}"
        } else {
            "${recipientUid}_${currentUserUid}"
        }

        // Escucha la sub-colección de mensajes en tiempo real, ordenados por fecha
        db.collection("chats").document(chatId!!)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Manejo de errores (puedes añadir un Log o un estado de error)
                    return@addSnapshotListener
                }

                val messageList = snapshot?.toObjects(ChatMessage::class.java) ?: emptyList()
                _messages.value = messageList
            }
    }

    fun sendMessage(text: String, recipientUid: String) {
        viewModelScope.launch {
            val currentUserUid = auth.currentUser?.uid ?: return@launch

            if (chatId == null) {
                // Esto es un seguro por si se intenta enviar un mensaje antes de cargar la conversación
                loadMessages(recipientUid)
            }

            val message = ChatMessage(
                senderId = currentUserUid,
                text = text,
                timestamp = Timestamp.now()
            )

            // 1. Añade el nuevo mensaje a la sub-colección de mensajes
            db.collection("chats").document(chatId!!)
                .collection("messages")
                .add(message)

            // 2. Actualiza el documento principal del chat con el último mensaje
            val chatInfo = mapOf(
                "lastMessage" to text,
                "lastMessageTimestamp" to Timestamp.now(),
                "participants" to listOf(currentUserUid, recipientUid)
            )
            // Usamos .merge() para asegurarnos de no borrar otros campos si existieran
            db.collection("chats").document(chatId!!)
                .set(chatInfo, SetOptions.merge())
        }
    }
}
