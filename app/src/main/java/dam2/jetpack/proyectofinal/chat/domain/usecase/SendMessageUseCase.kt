package dam2.jetpack.proyectofinal.chat.domain.usecase

import dam2.jetpack.proyectofinal.chat.domain.repository.ChatRepository
import javax.inject.Inject

/**
 * Caso de uso para enviar un mensaje en un chat.
 *
 * Esta clase encapsula la lógica para enviar un mensaje, delegando la operación
 * directamente al [ChatRepository].
 *
 * @property repo El repositorio que gestiona las operaciones de datos del chat.
 */
class SendMessageUseCase @Inject constructor(
    private val repo: ChatRepository
) {
    /**
     * Ejecuta el caso de uso para enviar un mensaje.
     *
     * La sobrecarga del operador `invoke` permite llamar a la clase como si fuera una función.
     *
     * @param eventId El ID del evento asociado al chat.
     * @param text El contenido del mensaje de texto a enviar.
     * @param otherUid El ID del otro participante en la conversación.
     */
    suspend operator fun invoke(eventId: String, text: String, otherUid: String) =
        repo.sendMessage(eventId, text, otherUid)
}
