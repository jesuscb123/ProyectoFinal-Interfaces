package dam2.jetpack.proyectofinal.events.domain.usecase

import androidx.compose.animation.core.copy
import dam2.jetpack.proyectofinal.events.domain.model.Event
import dam2.jetpack.proyectofinal.events.domain.repository.EventRepository
import javax.inject.Inject

/**
 * Caso de uso para aceptar un evento.
 *
 * Esta clase encapsula la lógica para marcar un evento como aceptado, delegando
 * la operación de guardado al repositorio correspondiente.
 *
 * @param repository El repositorio de eventos que se utilizará para actualizar el evento.
 */
class AcceptEventUseCase @Inject constructor(
    private val repository: EventRepository
) {
    /**
     * Ejecuta el caso de uso.
     *
     * Al invocar esta función (usando la sintaxis de `useCase(event)`), se actualizará
     * el evento en el repositorio.
     *
     * @param event El evento que se va a marcar como aceptado.
     */
    suspend operator fun invoke(event: Event) {
        repository.acceptEvent(event)
    }
}
