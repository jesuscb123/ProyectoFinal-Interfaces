package dam2.jetpack.proyectofinal.events.domain.usecase

import dam2.jetpack.proyectofinal.events.domain.model.Event
import dam2.jetpack.proyectofinal.events.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso para obtener los eventos en los que un usuario ha sido aceptado.
 *
 * Esta clase encapsula la lógica para recuperar una lista de eventos asociados a un
 * usuario específico, basándose en el estado de aceptación. Delega la obtención
 * de datos al [EventRepository].
 *
 * @property eventRepository El repositorio que gestiona las operaciones de datos para los eventos.
 */
class GetEventsUserUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    /**
     * Ejecuta el caso de uso para obtener los eventos de un usuario aceptado.
     *
     * La sobrecarga del operador `invoke` permite llamar a la clase como si fuera una función.
     *
     * @param userAccept El identificador del usuario para el cual se buscan los eventos.
     * @return Un [Flow] que emite una lista de objetos [Event] en los que el usuario ha sido aceptado.
     */
    operator fun invoke(userAccept: String): Flow<List<Event>> {
       return eventRepository.getEventsUser(userAccept)
    }
}