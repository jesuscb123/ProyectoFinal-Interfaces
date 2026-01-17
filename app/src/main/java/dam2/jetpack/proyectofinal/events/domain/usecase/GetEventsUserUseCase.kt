package dam2.jetpack.proyectofinal.events.domain.usecase

import dam2.jetpack.proyectofinal.events.domain.repository.EventRepository
import javax.inject.Inject

class GetEventsUserUseCase @Inject constructor(
    val eventRepository: EventRepository
) {
    suspend operator fun invoke(userAccept: String) = eventRepository.getEventsUser(userAccept)
}