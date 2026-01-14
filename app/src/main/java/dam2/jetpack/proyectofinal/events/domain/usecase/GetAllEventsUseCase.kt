package dam2.jetpack.proyectofinal.events.domain.usecase

import dam2.jetpack.proyectofinal.events.domain.model.Event
import dam2.jetpack.proyectofinal.events.domain.repository.EventRepository
import javax.inject.Inject

class GetAllEventsUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(): Result<List<Event>>{
        return try{
            val events = eventRepository.getAllEvents()
            Result.success(events)
        }catch (e: Exception){
            Result.failure(e)

        }
    }
}