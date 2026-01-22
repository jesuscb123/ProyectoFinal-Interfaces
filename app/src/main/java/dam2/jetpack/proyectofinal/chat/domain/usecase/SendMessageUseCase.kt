package dam2.jetpack.proyectofinal.chat.domain.usecase

import dam2.jetpack.proyectofinal.chat.domain.repository.ChatRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repo: ChatRepository
) {
    suspend operator fun invoke(eventId: String, text: String, otherUid: String) =
        repo.sendMessage(eventId, text, otherUid)
}
