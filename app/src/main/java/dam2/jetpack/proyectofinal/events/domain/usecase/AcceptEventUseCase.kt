package dam2.jetpack.proyectofinal.events.domain.usecase

import androidx.compose.animation.core.copy
import dam2.jetpack.proyectofinal.events.domain.model.Event
import dam2.jetpack.proyectofinal.events.domain.repository.EventRepository
import javax.inject.Inject

class AcceptEventUseCase @Inject constructor(
    private val repository: EventRepository
) {
    suspend operator fun invoke(event: Event) {
        repository.acceptEvent(event)
    }
}