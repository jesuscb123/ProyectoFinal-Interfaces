package dam2.jetpack.proyectofinal.events.domain.usecase

import dam2.jetpack.proyectofinal.events.domain.model.Event
import dam2.jetpack.proyectofinal.events.domain.repository.EventRepository
import javax.inject.Inject

/**
 * Caso de uso para obtener un evento específico por su ID.
 *
 * Esta clase encapsula la lógica para recuperar un único evento desde el repositorio.
 * Maneja los posibles errores y devuelve un [Result] que indica el éxito o
 * fracaso de la operación.
 *
 * @param eventRepository El repositorio de eventos desde el cual se obtendrá el evento.
 */
class GetEventByIdUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    /**
     * Ejecuta el caso de uso para obtener un evento por su ID.
     *
     * @param eventId El ID del evento a buscar.
     * @return Un [Result] que contiene el [Event] si se encuentra, o una excepción
     * en caso de fallo (e.g., si el evento no existe).
     */
    suspend operator fun invoke(eventId: Long): Result<Event?> {
        return try{
            val event = eventRepository.getById(eventId)
            Result.success(event)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}
