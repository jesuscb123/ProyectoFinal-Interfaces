package dam2.jetpack.proyectofinal.events.presentation.state

import dam2.jetpack.proyectofinal.events.domain.model.Event

/**
 * Representa el estado de la UI para la pantalla de eventos.
 *
 * Esta clase de datos contiene toda la información necesaria para que la UI se dibuje
 * correctamente en cualquier momento, incluyendo la lista de eventos, estados de carga,
 * errores y si la lista está vacía.
 *
 * @property events La lista de eventos [Event] que se debe mostrar en la UI.
 * @property isLoading Un booleano que es `true` cuando se están cargando los datos (e.g., desde el repositorio),
 *                     permitiendo mostrar un indicador de progreso.
 * @property errorMessage Un mensaje de error opcional que se puede mostrar al usuario si algo sale mal.
 * @property isEmpty Un booleano que es `true` si la lista de eventos está vacía, para poder mostrar un estado de "no hay datos".
 */
data class EventUiState(
    val events: List<Event> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isEmpty: Boolean = false,
    val completedEventsCount: Int = 0,
    val acceptedEventsCount: Int = 0
)
