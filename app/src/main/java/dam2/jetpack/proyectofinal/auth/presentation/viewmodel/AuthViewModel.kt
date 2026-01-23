package dam2.jetpack.proyectofinal.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dam2.jetpack.proyectofinal.auth.domain.useCase.LogOutUseCase
import dam2.jetpack.proyectofinal.auth.domain.useCase.LoginUseCase
import dam2.jetpack.proyectofinal.auth.domain.useCase.RegisterUseCase
import dam2.jetpack.proyectofinal.auth.presentation.state.AuthUiState
import dam2.jetpack.proyectofinal.user.domain.model.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para gestionar la lógica y el estado de las pantallas de autenticación (Login y Registro).
 *
 * Anotado con `@HiltViewModel` para que Hilt pueda inyectar las dependencias necesarias,
 * como los casos de uso de autenticación.
 *
 * @property loginUseCase Caso de uso para el inicio de sesión.
 * @property registerUseCase Caso de uso para el registro de nuevos usuarios.
 * @property logOutUseCase Caso de uso para cerrar la sesión del usuario.
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    val loginUseCase: LoginUseCase,
    val registerUseCase: RegisterUseCase,
    val logOutUseCase: LogOutUseCase
): ViewModel() {
    // Flujo de estado mutable y privado que contiene el estado actual de la UI.
    private val _uiState = MutableStateFlow(AuthUiState())
    // Flujo de estado público e inmutable expuesto a la UI para observar los cambios.
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    /**
     * Inicia el proceso de inicio de sesión con las credenciales proporcionadas.
     *
     * Actualiza el estado de la UI para reflejar el estado de carga y el resultado (éxito o error)
     * de la operación.
     *
     * @param email El correo electrónico del usuario.
     * @param password La contraseña del usuario.
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)

            val result = loginUseCase(email, password)

            _uiState.value = result.fold(
                onSuccess = {
                    _uiState.value.copy(isLoading = false, isAuthenticated = true, error = null)
                },
                onFailure = {
                    _uiState.value.copy(isLoading = false, error = it.message ?: "Error al iniciar sesión.")
                }
            )
        }
    }

    /**
     * Inicia el proceso de registro de un nuevo usuario.
     *
     * Actualiza el estado de la UI para reflejar el estado de carga y el resultado de la operación.
     * Tras un registro exitoso, marca al usuario como autenticado.
     *
     * @param email El correo electrónico para el nuevo usuario.
     * @param password La contraseña para el nuevo usuario.
     * @param rol El rol asignado al nuevo usuario (USER o ADMIN).
     */
    fun register(email: String, password: String, rol: Rol) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)

            val result = registerUseCase(email, password, rol)

            _uiState.value = result.fold(
                onSuccess = {
                    _uiState.value.copy(isLoading = false, isAuthenticated = true, error = null)
                },
                onFailure = { e ->
                    _uiState.value.copy(isLoading = false, error = e.message ?: "Error al registrase.")
                }
            )
        }
    }

    /**
     * Cierra la sesión del usuario actual.
     *
     * Actualiza el estado de la UI para reflejar que el usuario ya no está autenticado
     * o para mostrar un error si el proceso falla.
     */
    fun logOut() {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)

            val result = logOutUseCase()

            _uiState.value = result.fold(
                onSuccess = {
                    _uiState.value.copy(isLoading = false, isAuthenticated = false, error = null)
                },
                onFailure = {
                    _uiState.value.copy(isLoading = false,  error = it.message ?: "Error al cerrar sesión.")
                }
            )
        }
    }
}