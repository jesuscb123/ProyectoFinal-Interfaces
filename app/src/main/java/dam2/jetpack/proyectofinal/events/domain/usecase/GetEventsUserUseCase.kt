package dam2.jetpack.proyectofinal.events.domain.usecase

import dam2.jetpack.proyectofinal.events.domain.model.Event
import dam2.jetpack.proyectofinal.events.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEventsUserUseCase @Inject constructor(
    val eventRepository: EventRepository
) {
    operator fun invoke(userAccept: String): Flow<List<Event>> {
       return eventRepository.getEventsUser(userAccept)
    }
}