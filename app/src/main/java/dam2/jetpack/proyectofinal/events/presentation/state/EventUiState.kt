package dam2.jetpack.proyectofinal.events.presentation.state

import dam2.jetpack.proyectofinal.events.domain.model.Event

data class EventUiState(
    val events: List<Event> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isEmpty: Boolean = false
)
