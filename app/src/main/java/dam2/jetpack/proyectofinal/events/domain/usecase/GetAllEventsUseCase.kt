package dam2.jetpack.proyectofinal.events.domain.usecase

import dam2.jetpack.proyectofinal.events.domain.model.Event
import dam2.jetpack.proyectofinal.events.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso para obtener todos los eventos.
 *
 * Esta clase encapsula la lógica para recuperar una lista completa de todos los eventos
 * desde el repositorio, devolviéndolos como un flujo de datos reactivo.
 *
 * @param eventRepository El repositorio de eventos desde el cual se obtendrán los datos.
 */
class GetAllEventsUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    /**
     * Ejecuta el caso de uso.
     *
     * Al invocar esta función (usando la sintaxis `useCase()`), se obtiene un [Flow] que
     * emite la lista actualizada de todos los eventos cada vez que hay un cambio.
     *
     * @return Un [Flow] que emite una lista de [Event].
     */
    suspend operator fun invoke(): Flow<List<Event>>{
        return eventRepository.getAllEvents()

    }
}
