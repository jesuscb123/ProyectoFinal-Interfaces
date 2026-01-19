package dam2.jetpack.proyectofinal.events.domain.usecase

import dam2.jetpack.proyectofinal.events.domain.model.Event
import dam2.jetpack.proyectofinal.events.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEventsUserCreateUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    operator fun invoke(userId: String): Flow<List<Event>> {
        return eventRepository.getEventsUserCreate(userId)
    }

}