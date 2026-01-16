package dam2.jetpack.proyectofinal.events.domain.usecase

import dam2.jetpack.proyectofinal.events.domain.repository.EventRepository
import javax.inject.Inject

class DeleteEventUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(eventId: Long): Result<Int> {
        return try {
            val deletedRows = eventRepository.deleteEvent(eventId)
            if (deletedRows == 0) {
                Result.failure(Exception("No se encontró ningún evento con el ID: $eventId para eliminar."))
            } else {
                Result.success(deletedRows)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
