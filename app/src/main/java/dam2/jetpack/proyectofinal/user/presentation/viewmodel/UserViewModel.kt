package dam2.jetpack.proyectofinal.user.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dam2.jetpack.proyectofinal.user.domain.model.User
import dam2.jetpack.proyectofinal.user.domain.usecase.GetAllUsersUseCase
import dam2.jetpack.proyectofinal.user.domain.usecase.GetUserByEmailUseCase
import dam2.jetpack.proyectofinal.user.domain.usecase.GetUserByFirebaseUidUseCase
import dam2.jetpack.proyectofinal.user.domain.usecase.SaveUserUseCase
import dam2.jetpack.proyectofinal.user.presentation.state.UserStateUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para gestionar el estado y la lógica de la UI relacionada con los usuarios.
 *
 * Esta clase se encarga de interactuar con los casos de uso del dominio para obtener y manipular
 * datos de usuario, y expone el estado resultante a través de un [StateFlow] para que la UI
 * pueda observarlo y reaccionar a los cambios.
 *
 * @param getUserByFirebaseUidUseCase Caso de uso para obtener un usuario por su UID de Firebase.
 * @param getUserByEmailUseCase Caso de uso para obtener un usuario por su email.
 * @param saveUserUseCase Caso de uso para guardar (crear o actualizar) un usuario.
 * @param getAllUsersUseCase Caso de uso para obtener una lista de todos los usuarios.
 */
@HiltViewModel
class UserViewModel @Inject constructor(
    val getUserByFirebaseUidUseCase: GetUserByFirebaseUidUseCase,
    val getUserByEmailUseCase: GetUserByEmailUseCase,
    val saveUserUseCase : SaveUserUseCase,
    val getAllUsersUseCase: GetAllUsersUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(UserStateUi())
    /**
     * Flujo de estado que emite el estado actual de la UI de usuario.
     * La UI observa este flujo para actualizarse en respuesta a los cambios.
     */
    val uiState: StateFlow<UserStateUi> = _uiState.asStateFlow()

    /**
     * Obtiene un usuario por su UID de Firebase y actualiza el estado de la UI.
     *
     * @param firebaseUid El UID de Firebase del usuario a buscar.
     */
    fun getUserByFirebaseUid(firebaseUid: String){
        viewModelScope.launch{
            _uiState.value = UserStateUi(isLoading = true)

            val result = getUserByFirebaseUidUseCase(firebaseUid)

            _uiState.value = result.fold(
                onSuccess = {
                    _uiState.value.copy(user = it, error = null)
                },
                onFailure = { e ->
                    _uiState.value.copy(user = null, error = e.message)
                }
            )
        }
    }

    /**
     * Obtiene un usuario por su dirección de email y actualiza el estado de la UI.
     *
     * @param email El email del usuario a buscar.
     */
    fun getUserByEmail(email: String){
        viewModelScope.launch {
            _uiState.value = UserStateUi(isLoading = true)

            val result = getUserByEmailUseCase(email)

            _uiState.value = result.fold(
                onSuccess = {
                    _uiState.value.copy(user = it, error = null)
                },
                onFailure = {
                    _uiState.value.copy(user = null, error = it.message)
                }
            )

        }
    }

    /**
     * Guarda la información de un usuario utilizando el caso de uso correspondiente.
     *
     * @param user El objeto [User] a guardar.
     */
    fun saveUser(user: User){
        viewModelScope.launch {
            saveUserUseCase(user)
        }
    }

    /**
     * Carga la lista completa de usuarios y la actualiza en el estado de la UI.
     * Se utiliza principalmente en pantallas de administrador.
     */
    fun loadUsers(){
        viewModelScope.launch {
            getAllUsersUseCase().collect { users ->
                _uiState.value = _uiState.value.copy(
                   isLoading = false,
                    users = users,
                    error = null
                )
            }
        }
    }

    fun clearUser() {
        _uiState.value = _uiState.value.copy(user = null)
    }


}
