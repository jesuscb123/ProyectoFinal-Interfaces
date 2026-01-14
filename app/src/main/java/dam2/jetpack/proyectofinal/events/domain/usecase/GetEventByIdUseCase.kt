package dam2.jetpack.proyectofinal.events.domain.usecase

import dam2.jetpack.proyectofinal.events.domain.model.Event
import dam2.jetpack.proyectofinal.events.domain.repository.EventRepository
import javax.inject.Inject

class GetEventByIdUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(eventId: Long): Result<Event?> {
        return try{
            val event = eventRepository.getById(eventId)
            Result.success(event)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}