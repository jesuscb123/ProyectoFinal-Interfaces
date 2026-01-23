package dam2.jetpack.proyectofinal.events.domain.usecase

import dam2.jetpack.proyectofinal.events.domain.model.Event
import dam2.jetpack.proyectofinal.events.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso para obtener todos los eventos creados por un usuario específico.
 *
 * Esta clase encapsula la lógica para recuperar una lista de eventos desde el repositorio
 * que fueron creados por un usuario concreto, devolviéndolos como un flujo de datos reactivo.
 *
 * @param eventRepository El repositorio de eventos desde el cual se obtendrán los datos.
 */
class GetEventsUserCreateUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    /**
     * Ejecuta el caso de uso.
     *
     * Al invocar esta función (usando la sintaxis de `useCase(userId)`), se obtiene un [Flow] que
     * emite la lista actualizada de eventos creados por el usuario especificado.
     *
     * @param userId El ID del usuario creador de los eventos.
     * @return Un [Flow] que emite una lista de [Event].
     */
    operator fun invoke(userId: String): Flow<List<Event>> {
        return eventRepository.getEventsUserCreate(userId)
    }

}
