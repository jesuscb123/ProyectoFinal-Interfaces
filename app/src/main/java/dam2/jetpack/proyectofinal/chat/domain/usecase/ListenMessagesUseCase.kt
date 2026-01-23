package dam2.jetpack.proyectofinal.chat.domain.usecase

import dam2.jetpack.proyectofinal.chat.domain.model.ChatMessage
import dam2.jetpack.proyectofinal.chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso para escuchar los mensajes de un chat en tiempo real.
 *
 * Esta clase encapsula la lógica para obtener un flujo de mensajes de un chat específico,
 * delegando la llamada directamente al [ChatRepository].
 *
 * @property repo El repositorio que gestiona las operaciones de datos del chat.
 */
class ListenMessagesUseCase @Inject constructor(
    private val repo: ChatRepository
) {
    /**
     * Ejecuta el caso de uso para escuchar mensajes.
     *
     * La sobrecarga del operador `invoke` permite llamar a la clase como si fuera una función.
     *
     * @param eventId El ID del evento asociado al chat.
     * @param otherUid El ID del otro participante en la conversación.
     * @return Un [Flow] que emite la lista de [ChatMessage] del chat.
     */
    operator fun invoke(eventId: String, otherUid: String) =
        repo.listenMessages(eventId, otherUid)
}
