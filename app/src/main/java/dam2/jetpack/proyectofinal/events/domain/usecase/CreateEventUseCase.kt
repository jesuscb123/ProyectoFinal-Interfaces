package dam2.jetpack.proyectofinal.events.domain.usecase

import dam2.jetpack.proyectofinal.events.domain.model.Event
import dam2.jetpack.proyectofinal.events.domain.repository.EventRepository
import javax.inject.Inject

/**
 * Caso de uso para crear un nuevo evento.
 *
 * Esta clase encapsula la lógica de negocio para la creación de un evento,
 * delegando la operación de persistencia al repositorio correspondiente.
 *
 * @param eventRepository El repositorio de eventos que se utilizará para crear el evento.
 */
class CreateEventUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    /**
     * Ejecuta el caso de uso para crear un evento.
     *
     * Al invocar esta función (usando la sintaxis de `useCase(event)`), se creará
     * un nuevo evento en el repositorio.
     *
     * @param event El objeto [Event] que se va to crear.
     */
    suspend operator fun invoke(event: Event) {
        eventRepository.createEvent(event)
    }
}
