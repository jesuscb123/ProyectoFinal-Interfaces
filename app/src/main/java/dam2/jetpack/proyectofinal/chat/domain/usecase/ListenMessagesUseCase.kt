package dam2.jetpack.proyectofinal.chat.domain.usecase

import dam2.jetpack.proyectofinal.chat.domain.model.ChatMessage
import dam2.jetpack.proyectofinal.chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListenMessagesUseCase @Inject constructor(
    private val repo: ChatRepository
) {
    operator fun invoke(eventId: String, otherUid: String) =
        repo.listenMessages(eventId, otherUid)
}

