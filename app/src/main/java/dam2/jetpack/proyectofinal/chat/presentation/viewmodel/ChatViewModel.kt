package dam2.jetpack.proyectofinal.chat.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val listenMessagesUseCase: ListenMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var listenJob: Job? = null

    fun loadMessages(eventId: String, otherUid: String) {
        listenJob?.cancel()
        listenJob = viewModelScope.launch {
            listenMessagesUseCase(eventId, otherUid).collect { list ->
                _messages.value = list
            }
        }
    }

    fun sendMessage(eventId: String, text: String, otherUid: String) {
        viewModelScope.launch {
            sendMessageUseCase(eventId, text, otherUid)
        }
    }


    fun stopListening() {
        listenJob?.cancel()
        listenJob = null
    }
}
