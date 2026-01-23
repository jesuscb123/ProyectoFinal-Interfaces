package dam2.jetpack.proyectofinal.events.presentation.viewModel

import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dam2.jetpack.proyectofinal.events.domain.model.Category
import dam2.jetpack.proyectofinal.events.domain.model.Event
import dam2.jetpack.proyectofinal.events.domain.usecase.*
import dam2.jetpack.proyectofinal.events.presentation.state.EventUiState
import dam2.jetpack.proyectofinal.user.domain.usecase.GetUserByEmailUseCase
import dam2.jetpack.proyectofinal.user.domain.usecase.SaveUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

/**
 * ViewModel para la gestión de la lógica de negocio y el estado de la UI relacionados con los eventos.
 *
 * Esta clase se encarga de coordinar las interacciones del usuario con la capa de datos a través de
 * los diferentes casos de uso inyectados. Expone el estado de la UI como un [StateFlow] para que
 * los Composables puedan observarlo y reaccionar a los cambios.
 *
 * @param getAllEventsUseCase Caso de uso para obtener todos los eventos.
 * @param getEventByIdUseCase Caso de uso para obtener un evento por su ID.
 * @param createEventUseCase Caso de uso para crear un nuevo evento.
 * @param deleteEventUseCase Caso de uso para eliminar un evento.
 * @param acceptEventUseCase Caso de uso para aceptar o actualizar un evento.
 * @param getEventsUserUseCase Caso de uso para obtener los eventos aceptados por un usuario.
 * @param getEventsUserCreateEventUseCase Caso de uso para obtener los eventos creados por un usuario.
 * @param getUserByEmailUseCase Caso de uso para obtener los datos de un usuario por su email.
 * @param saveUserUseCase Caso de uso para guardar/actualizar los datos de un usuario.
 * @param getEventStatsUseCase Caso de uso para obtener las estadísticas de los eventos.
 */
@HiltViewModel
class EventViewModel @Inject constructor(
    private val getAllEventsUseCase: GetAllEventsUseCase,
    private val getEventByIdUseCase: GetEventByIdUseCase,
    private val createEventUseCase: CreateEventUseCase,
    private val deleteEventUseCase: DeleteEventUseCase,
    private val acceptEventUseCase: AcceptEventUseCase,
    private val getEventsUserUseCase: GetEventsUserUseCase,
    private val getEventsUserCreateEventUseCase: GetEventsUserCreateUseCase,
    private val getUserByEmailUseCase: GetUserByEmailUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    // --- CAMBIO: Guardar el caso de uso como una propiedad de la clase ---
    private val getEventStatsUseCase: GetEventStatsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(EventUiState())
    /**
     * Flujo de estado público que expone el estado actual de la UI de eventos.
     * Los Composables pueden recolectar este flujo para redibujarse cuando el estado cambie.
     */
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    // --- CÓDIGO NUEVO ---
    /**
     * Carga las estadísticas de eventos (completados y aceptados) y actualiza el estado de la UI.
     * Esta función es llamada desde la pantalla de administrador.
     */
    fun loadEventStats() {
        viewModelScope.launch {
            // Opcional: Indicar que se están cargando los datos
            _uiState.update { it.copy(isLoading = true) }

            // Llama al caso de uso para obtener los datos reales desde el repositorio
            val result = getEventStatsUseCase()

            result.onSuccess { (completed, accepted) ->
                // Si la operación es exitosa, actualiza el estado con los contadores
                _uiState.update {
                    it.copy(
                        completedEventsCount = completed,
                        acceptedEventsCount = accepted,
                        isLoading = false
                    )
                }
            }.onFailure { exception ->
                // Si ocurre un error, actualiza el estado para reflejarlo
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error al cargar estadísticas: ${exception.message}"
                    )
                }
            }
        }
    }
    // --- FIN DEL CÓDIGO NUEVO ---

    /**
     * Carga todos los eventos desde el repositorio y actualiza el estado de la UI.
     */
    fun loadEvents() {
        viewModelScope.launch {
            getAllEventsUseCase().collect { events ->
                _uiState.update {
                    it.copy(
                        events = events,
                        isEmpty = events.isEmpty(),
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }
        }
    }

    /**
     * Obtiene un evento específico por su ID.
     * @param eventId El ID del evento a buscar.
     */
    fun getEventById(eventId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = getEventByIdUseCase(eventId)

            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(errorMessage = null, isLoading = false) }
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(
                            events = emptyList(),
                            errorMessage = e.message,
                            isLoading = false
                        )
                    }
                }
            )
        }
    }

    /**
     * Crea un nuevo evento y lo guarda en el repositorio.
     *
     * @param userId El email del usuario que crea el evento.
     * @param tituloEvento El título para el nuevo evento.
     * @param descripcionEvento La descripción del nuevo evento.
     * @param categoria La categoría del evento.
     * @param resuelto El estado inicial de resolución del evento.
     */
    fun createEvent(
        userId: String?, // Este es el email
        tituloEvento: String,
        descripcionEvento: String,
        categoria: Category,
        resuelto: Boolean
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val currentUser = FirebaseAuth.getInstance().currentUser

            if (userId != null && currentUser != null) {
                val event = Event(
                    userId = userId,
                    creatorUid = currentUser.uid,
                    tituloEvento = tituloEvento,
                    descripcionEvento = descripcionEvento,
                    fechaCreacion = Date(),
                    categoria = categoria,
                    resuelto = resuelto,
                    userAccept = null,
                    userAcceptUid = null
                )

                try {
                    createEventUseCase(event)
                    loadEvents()

                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Error al guardar el evento: ${e.message}"
                        )
                    }
                }

            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error: Usuario no autenticado. No se puede crear el evento."
                    )
                }
            }
        }
    }

    /**
     * Elimina un evento por su ID.
     * @param eventId El ID del evento a eliminar.
     */
    fun deleteEvent(eventId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = deleteEventUseCase(eventId)

            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false) }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "Error al eliminar el evento"
                        )
                    }
                }
            )
        }
    }

    /**
     * Asigna el usuario actual a un evento, marcándolo como "aceptado".
     *
     * @param event El evento que se va a aceptar.
     * @param userEmail El email del usuario que acepta el evento.
     */
    @OptIn(UnstableApi::class)
    fun acceptEvent(
        event: Event,
        userEmail: String
    ) {
        viewModelScope.launch {
            val currentUid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch

            val updatedEvent = event.copy(
                userAccept = userEmail,
                userAcceptUid = currentUid
            )
            acceptEventUseCase(updatedEvent)
        }
    }

    /**
     * Desvincula al usuario de un evento, quitando la marca de "aceptado".
     * @param event El evento del que se va a cancelar la aceptación.
     */
    fun cancelAcceptance(event: Event) {
        viewModelScope.launch {
            val updatedEvent = event.copy(
                userAccept = null,
                userAcceptUid = null
            )
            acceptEventUseCase(updatedEvent)
        }
    }

    /**
     * Obtiene y muestra los eventos que un usuario específico ha aceptado.
     * @param userAccept El email del usuario para filtrar los eventos.
     */
    @OptIn(UnstableApi::class)
    fun getEventsUser(userAccept: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getEventsUserUseCase(userAccept).collect { events ->
                _uiState.update {
                    it.copy(
                        events = events,
                        isEmpty = events.isEmpty(),
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }
        }
    }

    /**
     * Obtiene y muestra los eventos que un usuario específico ha creado.
     * @param userId El email del usuario para filtrar los eventos.
     */
    fun getEventsUserCreate(userId: String){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getEventsUserCreateEventUseCase(userId).collect { events ->
                _uiState.update {
                    it.copy(
                        events = events,
                        isEmpty = events.isEmpty(),
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }
        }
    }

    /**
     * Marca un evento como resuelto y recompensa al usuario que lo aceptó con puntos.
     *
     * @param event El evento a marcar como resuelto.
     * @param pointsToAdd La cantidad de puntos a sumar al usuario que resolvió el evento.
     */
    @OptIn(UnstableApi::class)
    fun markEventAsResolved(event: Event, pointsToAdd: Int) {

        if (event.resuelto) return

        viewModelScope.launch(Dispatchers.IO) {

            val updatedEvent = event.copy(resuelto = true)

            acceptEventUseCase(updatedEvent)


            val userEmailToReward = event.userAccept


            if (userEmailToReward != null) {

                val userResult = getUserByEmailUseCase(userEmailToReward)

                userResult.onSuccess { user ->
                    if (user != null) {
                        val updatedUser = user.copy(puntos = user.puntos + pointsToAdd)
                        saveUserUseCase(updatedUser)
                    }
                }.onFailure { exception ->
                    Log.e("EventViewModel", "Error al buscar o guardar usuario para recompensa: ${exception.message}")
                }
            }

            withContext(Dispatchers.Main) {
                _uiState.update { uiStateValue ->
                    uiStateValue.copy(
                        events = uiStateValue.events.map { eventInList ->
                            if (eventInList.eventId == updatedEvent.eventId) {
                                updatedEvent
                            } else {
                                eventInList
                            }
                        }
                    )
                }
            }
        }
    }
}
