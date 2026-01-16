package dam2.jetpack.proyectofinal.events.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dam2.jetpack.proyectofinal.events.domain.model.Category
import dam2.jetpack.proyectofinal.events.domain.model.Event
import dam2.jetpack.proyectofinal.events.domain.usecase.AcceptEventUseCase
import dam2.jetpack.proyectofinal.events.domain.usecase.CreateEventUseCase
import dam2.jetpack.proyectofinal.events.domain.usecase.DeleteEventUseCase
import dam2.jetpack.proyectofinal.events.domain.usecase.GetAllEventsUseCase
import dam2.jetpack.proyectofinal.events.domain.usecase.GetEventByIdUseCase
import dam2.jetpack.proyectofinal.events.presentation.state.EventUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val getAllEventsUseCase: GetAllEventsUseCase,
    private val getEventByIdUseCase: GetEventByIdUseCase,
    private val createEventUseCase: CreateEventUseCase,
    private val deleteEventUseCase: DeleteEventUseCase,
    private val acceptEventUseCase: AcceptEventUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(EventUiState())
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    init{
        observeEvents()
    }
    private fun observeEvents() {
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
    userId: String?,
    tituloEvento: String,
    descripcionEvento: String,
    categoria: Category,
    resuelto: Boolean) {
    viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isLoading = true)

      if (userId != null){
          val event = Event(
              userId = userId,
              tituloEvento = tituloEvento,
              descripcionEvento = descripcionEvento,
              fechaCreacion = Date(),
              categoria = categoria,
              resuelto = resuelto,
              userAccept = null
          )
          createEventUseCase(event)
      }

    _uiState.value = _uiState.value.copy(isLoading = false)
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

    fun acceptEvent(event: Event, userEmail: String) {
        viewModelScope.launch {
            try {
                acceptEventUseCase(event, userEmail)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al aceptar el evento: ${e.message}"
                )
            }
        }
    }
}

