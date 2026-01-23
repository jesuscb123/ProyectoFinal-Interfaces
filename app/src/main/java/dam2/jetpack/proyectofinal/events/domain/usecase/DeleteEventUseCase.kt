package dam2.jetpack.proyectofinal.events.domain.usecase

import dam2.jetpack.proyectofinal.events.domain.repository.EventRepository
import javax.inject.Inject

/**
 * Caso de uso para eliminar un evento.
 *
 * Esta clase encapsula la lógica de negocio para eliminar un evento por su ID,
 * utilizando el repositorio correspondiente para realizar la operación.
 * Maneja los posibles errores y devuelve un [Result] que indica el éxito o
 * fracaso de la operación.
 *
 * @param eventRepository El repositorio de eventos desde el cual se eliminará el evento.
 */
class DeleteEventUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    /**
     * Ejecuta el caso de uso para eliminar un evento por su ID.
     *
     * @param eventId El ID del evento que se va a eliminar.
     * @return Un [Result] que contiene el número de filas eliminadas en caso de éxito,
     * o una excepción en caso de fallo (e.g., si el evento no se encuentra).
     */
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
