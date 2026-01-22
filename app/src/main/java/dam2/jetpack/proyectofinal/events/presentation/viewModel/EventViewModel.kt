package dam2.jetpack.proyectofinal.events.presentation.viewModel

import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dam2.jetpack.proyectofinal.events.domain.model.Category
import dam2.jetpack.proyectofinal.events.domain.model.Event
import dam2.jetpack.proyectofinal.events.domain.usecase.AcceptEventUseCase
import dam2.jetpack.proyectofinal.events.domain.usecase.CreateEventUseCase
import dam2.jetpack.proyectofinal.events.domain.usecase.DeleteEventUseCase
import dam2.jetpack.proyectofinal.events.domain.usecase.GetAllEventsUseCase
import dam2.jetpack.proyectofinal.events.domain.usecase.GetEventByIdUseCase
import dam2.jetpack.proyectofinal.events.domain.usecase.GetEventsUserCreateUseCase
import dam2.jetpack.proyectofinal.events.domain.usecase.GetEventsUserUseCase
import dam2.jetpack.proyectofinal.events.presentation.state.EventUiState
import dam2.jetpack.proyectofinal.user.domain.usecase.GetUserByEmailUseCase
import dam2.jetpack.proyectofinal.user.domain.usecase.SaveUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

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
    private val saveUserUseCase: SaveUserUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(EventUiState())
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    fun loadEvents() {
        viewModelScope.launch {
            getAllEventsUseCase().collect { events ->
                _uiState.value = _uiState.value.copy(
                    events = events,
                    isEmpty = events.isEmpty(),
                    isLoading = false,
                    errorMessage = null
                )
            }
        }
    }

    fun getEventById(eventId: Long) {
        viewModelScope.launch {
            _uiState.value = EventUiState(isLoading = true)

            val result = getEventByIdUseCase(eventId)

            _uiState.value = result.fold(
                onSuccess = {
                    _uiState.value.copy(errorMessage = null, isLoading = false)
                },
                onFailure = { e ->
                    _uiState.value.copy(
                        events = emptyList(),
                        errorMessage = e.message,
                        isLoading = false
                    )
                }
            )
        }
    }

    fun createEvent(
        userId: String?, // Este es el email
        tituloEvento: String,
        descripcionEvento: String,
        categoria: Category,
        resuelto: Boolean
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
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
                    userAccept = null
                )

                // --- ¡AQUÍ ESTÁ EL CAMBIO CLAVE! ---
                // Primero, ejecutamos el UseCase para crear el evento.
                // Asumimos que createEventUseCase es una función 'suspend'.
                try {
                    createEventUseCase(event) // Esperamos a que esta operación termine.

                    // UNA VEZ que el guardado ha terminado con éxito...
                    // ...AHORA sí recargamos la lista de eventos.
                    loadEvents() // Reutilizamos tu función loadEvents() que ya hace esto perfectamente.

                } catch (e: Exception) {
                    // Si el guardado falla, lo notificamos.
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Error al guardar el evento: ${e.message}"
                    )
                }

            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error: Usuario no autenticado. No se puede crear el evento."
                )
            }
        }
    }



        fun deleteEvent(eventId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val result = deleteEventUseCase(eventId)

            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Error al eliminar el evento"
                    )
                }
            )
        }
    }

    @OptIn(UnstableApi::class)
    fun acceptEvent(
        event: Event,
        userEmail: String
    ) {
        viewModelScope.launch {
            val updatedEvent = event.copy(
                userAccept = userEmail
            )
            acceptEventUseCase(updatedEvent)
        }
    }

    fun cancelAcceptance(event: Event) {
        viewModelScope.launch {
            val updatedEvent = event.copy(
                userAccept = null
            )
            acceptEventUseCase(updatedEvent)
        }
    }

    @OptIn(UnstableApi::class)
    fun getEventsUser(userAccept: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getEventsUserUseCase(userAccept).collect { events ->
                _uiState.value = _uiState.value.copy(
                    events = events,
                    isEmpty = events.isEmpty(),
                    isLoading = false,
                    errorMessage = null
                )
            }
        }
    }

    fun getEventsUserCreate(userId: String){
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getEventsUserCreateEventUseCase(userId).collect { events ->
                _uiState.value = _uiState.value.copy(
                    events = events,
                    isEmpty = events.isEmpty(),
                    isLoading = false,
                    errorMessage = null
                )
            }
        }
    }

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
                _uiState.value = _uiState.value.copy(
                    events = _uiState.value.events.map { eventInList ->
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


