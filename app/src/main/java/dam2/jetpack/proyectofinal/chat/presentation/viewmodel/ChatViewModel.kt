package dam2.jetpack.proyectofinal.chat.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dam2.jetpack.proyectofinal.chat.domain.model.ChatMessage
import dam2.jetpack.proyectofinal.chat.domain.usecase.ListenMessagesUseCase
import dam2.jetpack.proyectofinal.chat.domain.usecase.SendMessageUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de Chat.
 *
 * Gestiona el estado de la UI del chat, incluyendo la lista de mensajes y los errores.
 * Se encarga de la lógica para cargar, enviar y escuchar mensajes a través de los
 * casos de uso correspondientes.
 *
 * @property listenMessagesUseCase Caso de uso para escuchar mensajes en tiempo real.
 * @property sendMessageUseCase Caso de uso para enviar un nuevo mensaje.
 */
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val listenMessagesUseCase: ListenMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase
) : ViewModel() {

    // Flujo de estado privado que contiene la lista de mensajes del chat actual.
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    /**
     * Flujo de estado público e inmutable con la lista de mensajes para ser observada por la UI.
     */
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    // Flujo de estado privado para manejar los errores.
    private val _error = MutableStateFlow<String?>(null)
    /**
     * Flujo de estado público con un posible mensaje de error para ser mostrado en la UI.
     */
    val error: StateFlow<String?> = _error.asStateFlow()

    // Job de corutina para la escucha activa de mensajes, permite cancelarlo cuando sea necesario.
    private var listenJob: Job? = null

    /**
     * Carga y empieza a escuchar los mensajes para un chat específico.
     *
     * Si ya hay una escucha activa, la cancela antes de iniciar una nueva.
     *
     * @param eventId El ID del evento asociado al chat.
     * @param otherUid El UID del otro participante del chat.
     */
    fun loadMessages(eventId: String, otherUid: String) {
        listenJob?.cancel()
        listenJob = viewModelScope.launch {
            listenMessagesUseCase(eventId, otherUid).collect { list ->
                _messages.value = list
            }
        }
    }

    /**
     * Envía un mensaje y actualiza la UI de forma optimista.
     *
     * 1. Crea un mensaje local temporalmente y lo añade inmediatamente a la lista de mensajes
     *    para que la UI se actualice al instante (actualización optimista).
     * 2. Llama al caso de uso para enviar el mensaje real a la base de datos.
     * 3. Si el envío falla, elimina el mensaje optimista de la lista para reflejar el estado real.
     *
     * @param eventId El ID del evento asociado al chat.
     * @param text El contenido del mensaje.
     * @param otherUid El UID del destinatario del mensaje.
     */
    fun sendMessage(eventId: String, text: String, otherUid: String) {
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // Actualización optimista: se crea un mensaje local antes de enviarlo
        val optimistic = ChatMessage(
            senderId = currentUid,
            text = text,
            timestamp = com.google.firebase.Timestamp.now()
        )

        // Se añade el mensaje a la lista local para una respuesta instantánea en la UI
        _messages.value = listOf(optimistic) + _messages.value

        viewModelScope.launch {
            runCatching {
                sendMessageUseCase(eventId, text, otherUid)
            }.onFailure {
                // Si falla el envío, se revierte la actualización optimista
                _messages.value = _messages.value.drop(1)
            }
        }
    }

    /**
     * Detiene la escucha de mensajes en tiempo real.
     *
     * Es importante llamar a este método (por ejemplo, en `onDispose` de un Composable)
     * para evitar fugas de memoria y listeners innecesarios.
     */
    fun stopListening() {
        listenJob?.cancel()
        listenJob = null
    }
}
